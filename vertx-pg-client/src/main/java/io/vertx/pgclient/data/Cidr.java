package io.vertx.pgclient.data;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

public class Cidr {
  private InetAddress address;
  private Integer netmask;


  public InetAddress getAddress(){
    return address;
  }
  public Cidr setAddress(InetAddress address){
    this.address=address;
    return this;
  }

  public Integer getNetmask(){
    return netmask;
  }

  public Cidr setNetmask(Integer netMask){
    if (netmask != null && ((getAddress() instanceof Inet4Address && (netmask < 0 || netmask > 32)) ||
        (getAddress() instanceof Inet6Address && (netmask < 0 || netmask > 128)))) {
        throw new IllegalArgumentException("Invalid netmask: " + netmask);

    }
    this.netmask = netMask;
    return this;
  }

}
