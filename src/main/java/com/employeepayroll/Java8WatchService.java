package com.employeepayroll;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Java8WatchService {
	public final WatchService watcher; 
	public final Map<WatchKey, Path> dirWatchers;
	
	//Creates a Watch Service and registers the given directory
	public Java8WatchService(Path dir) throws IOException{
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dirWatchers = new HashMap<WatchKey,Path>();
		scanAndRegisterDir(dir);
	}
	
	//Register the given Directory with Watch Service
	public void registerDirWatchers(Path dir) throws IOException{
		WatchKey key = dir.register(watcher,
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		dirWatchers.put(key, dir);
	}
	
	//Register the given Directory and sub-Directories with Watch Service
	public void scanAndRegisterDir(final Path start) throws IOException {
		Files.walkFileTree(start, new SimpleFileVisitor<Path>(){
		@Override
		public FileVisitResult preVisitDirectory(Path dir,
				BasicFileAttributes attrs) throws IOException{
			registerDirWatchers(dir);
			return FileVisitResult.CONTINUE;
		}
		});
	}
	
	//Process all events for keys queued for watchers
	public boolean processEvents() {
		while(true) {
			WatchKey key;		//Waiting for key to be signalled
			try {
				key = watcher.take();
			}catch(InterruptedException exception) {
				return false;
			}
			Path dir = dirWatchers.get(key);
			if(dir==null) continue;
			for(WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				Path name = ((WatchEvent<Path>)event).context();
				Path child = dir.resolve(name);
				System.out.format("%s : %s\n", event.kind().name(),child);
						//Printing out event
				
				//If directory created -> register it and sub Dir
				if(kind == StandardWatchEventKinds.ENTRY_CREATE) {
					try {
						if(Files.isDirectory(child)) scanAndRegisterDir(child);
					}catch (IOException exception) {}
				}else if(kind.equals(StandardWatchEventKinds.ENTRY_DELETE)) {
					if(Files.isDirectory(child)) dirWatchers.remove(key);
				}
			}
			
			//reset key
			boolean valid = key.reset();
			if(!valid) {
				dirWatchers.remove(key);
				if(dirWatchers.isEmpty()) break; 	//all Dirs inaccessible
			}
		}
		return true;
	}
}