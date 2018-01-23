package com.example.demo.gateway.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class IpUserHashRule extends AbstractLoadBalancerRule {
	
	private static Logger log = LoggerFactory.getLogger(IpUserHashRule.class);

	public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }

        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            List<Server> sortedServers = new ArrayList<Server>();
            sortedServers.addAll(reachableServers);
            Collections.sort(sortedServers, Comparator.comparing(Server::getId));
            int upCount = reachableServers.size();
            int serverCount = reachableServers.size();
            System.out.println("key: "+ key+ " upCount: "+upCount + " serverCount: "+serverCount);	
            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }

            int nextServerIndex = ipUserHash(serverCount);
            
            System.out.println("key: "+ key+ " upCount: "+upCount + " serverCount: "+serverCount + " nextServerIndex: "+nextServerIndex);
            server = sortedServers.get(nextServerIndex);
            System.out.println("server:" + server.getId() + " port" + ((DiscoveryEnabledServer)server).getInstanceInfo().getAppGroupName());
            System.out.println("server info:" + server.toString());
            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            else {
            	Thread.yield();
                continue;
            }
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;

    }

	private int ipUserHash(int serverCount) {
		String userIp = getRemoteAddr();
        try {
        	userIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
		}
        int userHashCode = Math.abs((userIp).hashCode());
		return userHashCode%serverCount;
	}

	private String getRemoteAddr() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String remoteAddr = "0.0.0.0";
        if (request.getHeader("X-FORWARDED-FOR") != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
        } else {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
	}

	@Override
	public Server choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	@Override
	public void initWithNiwsConfig(IClientConfig clientConfig) {
		// TODO Auto-generated method stub
		
	}
}
