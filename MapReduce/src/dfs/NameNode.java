package dfs;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import util.*;

public class NameNode {
	ArrayList<String> slaveaddr;
	HashMap<String, ArrayList<String>> filenametoslaveaddr;
	private int replication;
	private int current;
	private int availableReplication;
	
	public NameNode(ArrayList<String> slaveaddr, int replication) {
		this.slaveaddr = slaveaddr;
		this.replication = replication;
		availableReplication = Math.min(replication, slaveaddr.size());
		filenametoslaveaddr = new HashMap<String, ArrayList<String>>();
		current = 0;
	}
	
	public Message decide(String filename, ObjectOutputStream oos) throws Exception {
		ArrayList<String> ret = new ArrayList<String>();
		if (filename.startsWith("r")) {
			String name = filename.substring(filename.indexOf("_"), filename.length() - 1);
			for (String key : filenametoslaveaddr.keySet()) {
				if (key.contains(name)) {
					ret = filenametoslaveaddr.get(key);
					filenametoslaveaddr.put(filename, ret);
					Message response = new Message();
					response.setAddrList(ret);
					return response;
				}
				
			}
		}
		for (int i = 0; i < availableReplication; i++) {
			ret.add(slaveaddr.get(current));
			current += 1;
			if (current >= slaveaddr.size()) {
				current = 0;
			}
		}
		filenametoslaveaddr.put(filename, ret);
		Message response = new Message();
		response.setAddrList(ret);
		return response;
	}
	
	public ArrayList<Message> slaveFailure(String slave) {
		ArrayList<Message> ret = new ArrayList<Message>();
		slaveaddr.remove(slave);
		availableReplication = Math.min(replication, slaveaddr.size());
		boolean add = true;
		if (availableReplication < replication) {
			add = false;
		}
		Iterator<Entry<String, ArrayList<String>>> it = filenametoslaveaddr.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>)it.next();  
			String key = entry.getKey(); 
			ArrayList<String> val = entry.getValue();
			boolean change = false;
			for (String v : val) {
				if (v.equals(slave)) {
					change = true;
				}
			}
			if (change) {
				val.remove(slave);
				if (add) {
					ArrayList<String> avail = new ArrayList<String>();
					for (String v : slaveaddr) {
						if (!val.contains(val)) {
							avail.add(v);
						}
					}
					Random ra =new Random();
					String source = val.get(ra.nextInt(val.size()));
					ra =new Random();
					String target = avail.get(ra.nextInt(avail.size()));
					Message message = new Message();
					message.setFileName(key);
					ArrayList<String> s = new ArrayList<String>();
					s.add(source);
					s.add(target);
					message.setAddrList(s);
					ret.add(message);
				}
			}
		}
		return ret;
	}
	
	public Message recover(String addr) {
		slaveaddr.add(addr);
		return null;
	}
	
	public String require(String filename, String addr) {
		String source = filenametoslaveaddr.get(filename).get(0);
		return source;
	}
	
	public ArrayList<String> findFile(String filename) {
		return filenametoslaveaddr.get(filename);
	}
	
	public void listAll() {
		if (filenametoslaveaddr.isEmpty()) {
			System.out.println("No files");
		} else {
			Iterator<Entry<String, ArrayList<String>>> it = filenametoslaveaddr.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>)it.next();  
				String key = entry.getKey(); 
				ArrayList<String> val = entry.getValue();
				System.out.println(key + ":");
				for (String v : val) {
					System.out.print(v + ";");
				}
				System.out.print("\n");
			}
		}
	}
}
