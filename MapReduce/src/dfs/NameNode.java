package dfs;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import util.*;
import master.*;

public class NameNode {
	ArrayList<String> slaveaddr;
	HashMap<String, ArrayList<String>> filenametoslaveaddr;
	private int replication;
	private int current;
	private int availableReplication;
	public MasterCoordinator coord;
	
	public NameNode(ArrayList<String> slaveaddr, int replication, MasterCoordinator coord) {
		this.slaveaddr = slaveaddr;
		this.replication = replication;
		availableReplication = Math.min(replication, slaveaddr.size());
		filenametoslaveaddr = new HashMap<String, ArrayList<String>>();
		current = 0;
		this.coord = coord;
	}
	
	public Message decide(String filename) throws Exception {
		ArrayList<String> ret = new ArrayList<String>();
		if (filename.startsWith("r")) {
			String name = filename.substring(filename.indexOf("_"), filename.length() - 1);
			for (String key : filenametoslaveaddr.keySet()) {
				if (key.contains(name)) {
					ret = filenametoslaveaddr.get(key);
					filenametoslaveaddr.put(filename, ret);
					Message response = new Message();
					response.setAddrList(ret);
					
				}
				
			}
		} else {
			for (int i = 0; i < availableReplication; i++) {
				ret.add(slaveaddr.get(current));
				current += 1;
				if (current >= slaveaddr.size()) {
				current = 0;
				}
			}
		}
		filenametoslaveaddr.put(filename, ret);
		Message response = new Message();
		response.setAddrList(ret);
		
		String origin = inputFileName(filename);
		int left = 0;
		Iterator<Entry<String, ArrayList<String>>> it = filenametoslaveaddr.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>)it.next();  
			String key = entry.getKey(); 
			if (inputFileName(key).equals(origin)) {
				if (key.startsWith("m")) {
					left += Configuration.numberOfReducers;		
				} else {
					left--;
				}
			}
		}
		System.out.println(left + "left");
		if (left == 0) {
			coord.canStartReduce();
			
		}
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
	
	public void recover(String addr) {
		slaveaddr.add(addr);
	}
	
	public String require(String filename, String addr) {
		String source = filenametoslaveaddr.get(filename).get(0);
		ArrayList<String> val = filenametoslaveaddr.get(filename);
		if (!val.contains(addr)) {
			val.add(addr);
			filenametoslaveaddr.put(filename, val);
		}
		return source;
	}
	
	public ArrayList<String> findFile(String filename) {
		if (filenametoslaveaddr.containsKey(filename)) {
			return filenametoslaveaddr.get(filename);
		}
		return new ArrayList<String>();
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
	
	public String inputFileName(String partitionFileName) {
		if (partitionFileName.startsWith("m")) {
			String[] split = partitionFileName.split("_", 2);
			return split[1];
		} else {
			String[] split = partitionFileName.split("_", 2);
			return split[1].substring(0, split[1].lastIndexOf("_"));
		}
	}
	
	
	public void deleteJob(String filename) {
		Iterator<Entry<String, ArrayList<String>>> it = filenametoslaveaddr.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = (Map.Entry<String, ArrayList<String>>)it.next();  
			String key = entry.getKey(); 
			if (inputFileName(key).equals(filename)) {
				filenametoslaveaddr.remove(key);
			}
		}

	}
	public void deleteAll() {
		filenametoslaveaddr.clear();
	}
}
