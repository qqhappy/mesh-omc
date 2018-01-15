/**
 * 
 */
package com.xinwei.minas.mcbts.core.model.layer3;

import java.io.Serializable;

/**
 * @author chenshaohua
 * 
 */
@SuppressWarnings("serial")
public class McBtsACL implements Serializable {

	// 协议列表
	public static final String[] PROTOCOL_LIST = {
			"0:HOPOPT, IPv6 Hop-by-Hop Option",
			"1:ICMP, Internet Control Message Protocol",
			"2:IGAP IGMP RGMP",
			"3:GGP, Gateway to Gateway Protocol",
			"4:IP in IP encapsulation",
			"5:ST, Internet Stream Protocol",
			"6:TCP, Transmission Control Protocol",
			"7:UCL, CBT",
			"8:EGP, Exterior Gateway Protocol",
			"9:IGRP, Interior Gateway Routing Protocol",
			"10:BBN RCC Monitoring",
			"11:NVP, Network Voice Protocol",
			"12:PUP",
			"13:ARGUS",
			"14:EMCON, Emission Control Protocol",
			"15:XNET, Cross Net Debugger",
			"16:Chaos",
			"17:UDP, User Datagram Protocol",
			"18:TMux, Transport Multiplexing Protocol",
			"19:DCN Measurement Subsystems",
			"20:HMP, Host Monitoring Protocol",
			"21:Packet Radio Measurement",
			"22:XEROX NS IDP",
			"23:Trunk-1",
			"24:Trunk-2",
			"25:Leaf-1",
			"26:Leaf-2",
			"27:RDP, Reliable Data Protocol",
			"28:IRTP, Internet Reliable Transaction Protocol",
			"29:ISO Transport Protocol Class 4",
			"30:NETBLT, Network Block Transfer",
			"31:MFE Network Services Protocol",
			"32:MERIT Internodal Protocol",
			"33:SEP,DCCP",
			"34:Third Party Connect Protocol",
			"35:IDPR, Inter-Domain Policy Routing Protocol",
			"36:XTP, Xpress Transfer Protocol",
			"37:Datagram Delivery Protocol",
			"38:IDPR, Control Message Transport Protocol",
			"39:TP++ Transport Protocol",
			"40:IL Transport Protocol",
			"41:IPv6 over IPv4",
			"42:SDRP, Source Demand Routing Protocol",
			"43:IPv6 Routing header",
			"44:IPv6 Fragment header",
			"45:IDRP, Inter-Domain Routing Protocol",
			"46:RSVP, Reservation Protocol",
			"47:GRE, General Routing Encapsulation",
			"48:MHRP, Mobile Host Routing Protocol",
			"49:BNA",
			"50:ESP, Encapsulating Security Payload",
			"51:AH, Authentication Header",
			"52:Integrated Net Layer Security TUBA",
			"53:IP with Encryption",
			"54:NARP, NBMA Address Resolution Protocol",
			"55:Minimal Encapsulation Protocol",
			"56:TLSP, Transport Layer Security Protocol using Kryptonet key management",
			"57:SKIP",
			"58:ICMPv6, Internet Control Message Protocol for IPv6.MLD, Multicast Listener Discovery",
			"59:IPv6 No Next Header",
			"60:IPv6 Destination Options",
			"61:Any host internal protocol",
			"62:CFTP",
			"63:Any local network",
			"64:SATNET and Backroom EXPAK",
			"65:Kryptolan",
			"66:MIT Remote Virtual Disk Protocol",
			"67:Internet Pluribus Packet Core",
			"68:Any distributed file system",
			"69:SATNET Monitoring",
			"70:VISA Protocol",
			"71:Internet Packet Core Utility",
			"72:Computer Protocol Network Executive",
			"73:Computer Protocol Heart Beat",
			"74:Wang Span Network",
			"75:Packet Video Protocol",
			"76:Backroom SATNET Monitoring",
			"77:SUN ND PROTOCOL-Temporary",
			"78:WIDEBAND Monitoring",
			"79:WIDEBAND EXPAK",
			"80:ISO-IP",
			"81:VMTP, Versatile Message Transaction Protocol",
			"82:SECURE-VMTP",
			"83:VINES",
			"84:TTP",
			"85:NSFNET-IGP",
			"86:Dissimilar Gateway Protocol",
			"87:TCF",
			"88:EIGRP",
			"89:OSPF, Open Shortest Path First Routing Protocol.MOSPF, Multicast Open Shortest Path First",
			"90:Sprite RPC Protocol", "91:Locus Address Resolution Protocol",
			"92:MTP, Multicast Transport Protocol", "93:AX.25",
			"94:IP-within-IP Encapsulation Protocol",
			"95:Mobile Internetworking Control Protocol",
			"96:Semaphore Communications Sec. Pro", "97:EtherIP",
			"98:Encapsulation Header", "99:Any private encryption scheme",
			"100:GMTP", "101:IFMP, Ipsilon Flow Management Protocol",
			"102:PNNI over IP", "103:PIM, Protocol Independent Multicast",
			"104:ARIS", "105:SCPS", "106:QNX", "107:Active Networks",
			"108:IPPCP, IP Payload Compression Protocol",
			"109:SNP, Sitara Networks Protocol", "110:Compaq Peer Protocol",
			"111:IPX in IP", "112:VRRP, Virtual Router Redundancy Protocol",
			"113:PGM, Pragmatic General Multicast", "114:any 0-hop protocol",
			"115:L2TP, Level 2 Tunneling Protocol",
			"116:DDX, D-II Data Exchange",
			"117:IATP, Interactive Agent Transfer Protocol",
			"118:ST, Schedule Transfer", "119:SRP, SpectraLink Radio Protocol",
			"120:UTI", "121:SMP, Simple Message Protocol", "122:SM",
			"123:PTP, Performance Transparency Protocol", "124:ISIS over IPv4",
			"125:FIRE", "126:CRTP, Combat Radio Transport Protocol",
			"127:CRUDP, Combat Radio User Datagram", "128:SSCOPMCE",
			"129:IPLT", "130:SPS, Secure Packet Shield",
			"131:PIPE, Private IP Encapsulation within IP",
			"132:SCTP, Stream Control Transmission Protocol",
			"133:Fibre Channel", "134:RSVP-E2E-IGNORE", "135:Mobility Header",
			"136:UDP-Lite, Lightweight User Datagram Protocol",
			"137:MPLS in IP" };
	// Operation列表
	public static final String[] OPERATION_LIST = { "EQ", "NEQ", "LT", "GT" };

	// 记录索引
	private Long idx;

	// moId
	private Long moId;

	// 协议
	private Integer protocol;

	// 源ip
	private Long sourceIP;

	// 源ip掩码
	private Long sourceIPMask;

	// 源端口
	private Integer sourcePort;

	// 源操作
	private Integer sourceOper;

	// 目标ip
	private Long desIp;

	// 目标ip掩码
	private Long desIPMask;

	// 目标端口
	private Integer desPort;

	// 目标操作
	private Integer desOper;

	// 允许/禁止
	private Integer permission;

	// 消息条数，不写入数据库
	private Integer entry;

	public McBtsACL() {
	}

	public Object getPropertyValueByName(String propertyName) {
		if (propertyName.equals("idx")) {
			return this.idx;
		}
		if (propertyName.equals("protocol")) {
			return this.protocol;
		}
		if (propertyName.equals("sourceIP")) {
			return this.sourceIP;
		}
		if (propertyName.equals("sourceIPMask")) {
			return this.sourceIPMask;
		}
		if (propertyName.equals("sourcePort")) {
			return this.sourcePort;
		}
		if (propertyName.equals("sourceOper")) {
			return this.sourceOper;
		}
		if (propertyName.equals("desIp")) {
			return this.desIp;
		}
		if (propertyName.equals("desIPMask")) {
			return this.desIPMask;
		}
		if (propertyName.equals("desPort")) {
			return this.desPort;
		}
		if (propertyName.equals("desOper")) {
			return this.desOper;
		}
		if (propertyName.equals("permission")) {
			return this.permission;
		}
		if (propertyName.equals("entry")) {
			return this.entry;
		}

		return null;
	}

	public Long getDesIp() {
		return desIp;
	}

	public void setDesIp(Long desIp) {
		this.desIp = desIp;
	}

	public Long getDesIPMask() {
		return desIPMask;
	}

	public void setDesIPMask(Long desIPMask) {
		this.desIPMask = desIPMask;
	}

	public Integer getDesOper() {
		return desOper;
	}

	public void setDesOper(Integer desOper) {
		this.desOper = desOper;
	}

	public Integer getDesPort() {
		return desPort;
	}

	public void setDesPort(Integer desPort) {
		this.desPort = desPort;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Integer getPermission() {
		return permission;
	}

	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	public Integer getProtocol() {
		return protocol;
	}

	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}

	public Long getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(Long sourceIP) {
		this.sourceIP = sourceIP;
	}

	public Long getSourceIPMask() {
		return sourceIPMask;
	}

	public void setSourceIPMask(Long sourceIPMask) {
		this.sourceIPMask = sourceIPMask;
	}

	public Integer getSourceOper() {
		return sourceOper;
	}

	public void setSourceOper(Integer sourceOper) {
		this.sourceOper = sourceOper;
	}

	public Integer getSourcePort() {
		return sourcePort;
	}

	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}

	public Long getMoId() {
		return moId;
	}

	public void setMoId(Long moId) {
		this.moId = moId;
	}

	public Integer getEntry() {
		return entry;
	}

	public void setEntry(Integer entry) {
		this.entry = entry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((desIPMask == null) ? 0 : desIPMask.hashCode());
		result = prime * result + ((desIp == null) ? 0 : desIp.hashCode());
		result = prime * result + ((desOper == null) ? 0 : desOper.hashCode());
		result = prime * result + ((desPort == null) ? 0 : desPort.hashCode());
		result = prime * result
				+ ((permission == null) ? 0 : permission.hashCode());
		result = prime * result
				+ ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result
				+ ((sourceIP == null) ? 0 : sourceIP.hashCode());
		result = prime * result
				+ ((sourceIPMask == null) ? 0 : sourceIPMask.hashCode());
		result = prime * result
				+ ((sourceOper == null) ? 0 : sourceOper.hashCode());
		result = prime * result
				+ ((sourcePort == null) ? 0 : sourcePort.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof McBtsACL))
			return false;
		McBtsACL other = (McBtsACL) obj;
		if (desIPMask == null) {
			if (other.desIPMask != null)
				return false;
		} else if (!desIPMask.equals(other.desIPMask))
			return false;
		if (desIp == null) {
			if (other.desIp != null)
				return false;
		} else if (!desIp.equals(other.desIp))
			return false;
		if (desOper == null) {
			if (other.desOper != null)
				return false;
		} else if (!desOper.equals(other.desOper))
			return false;
		if (desPort == null) {
			if (other.desPort != null)
				return false;
		} else if (!desPort.equals(other.desPort))
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (sourceIP == null) {
			if (other.sourceIP != null)
				return false;
		} else if (!sourceIP.equals(other.sourceIP))
			return false;
		if (sourceIPMask == null) {
			if (other.sourceIPMask != null)
				return false;
		} else if (!sourceIPMask.equals(other.sourceIPMask))
			return false;
		if (sourceOper == null) {
			if (other.sourceOper != null)
				return false;
		} else if (!sourceOper.equals(other.sourceOper))
			return false;
		if (sourcePort == null) {
			if (other.sourcePort != null)
				return false;
		} else if (!sourcePort.equals(other.sourcePort))
			return false;
		return true;
	}

}
