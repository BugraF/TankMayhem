///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
//// */
//
//package game;
//import java.io.File;
//import java.io.InputStream;
//import java.util.Map;
//
//import javax.swing.JOptionPane;
//
//import javazoom.jl.player.Player;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
//
//
/////**
//// *
//// * @author Turac Abbaszade
//// 
//public class SoundManager {
//     public SoundManager(){}
//	
//
//	
//	//Fields for Sound
//	private Map<String, AudioStream> sounds;
//	AudioStream audious;
//	
//	//Fields for Music
//	private final static int NOTSTARTED = 0;
//    private final static int PLAYING = 1;
//    private final static int PAUSED = 2;
//    private final static int FINISHED = 3;
//   
//    // the player actually doing all the work
//    private Player player;
//    // locking object used to communicate with player thread
//    private final Object playerLock = new Object();
//    // status variable what player thread is doing/supposed to do
//    private int playerStatus = NOTSTARTED;
//    private String musicFolder;
//	final int MAX_SIZE = 50;
//	private int[][] playOrder = new int[MAX_SIZE][MAX_SIZE];
//	private int [] indices = new int []{0,0,0,0};
//	private String[][] modeNamePath= new String[MAX_SIZE][MAX_SIZE];
//	boolean isFinished = false;
//	int currentMusic = 0;
//	int [] maxNumOfFiles = new int []{0,0,0,0};
//	public static String currentModeName;
//	public String[] modes= new String[] {"Game", "Assault", "Armored", "Balanced"};
//	String musicName="";
//	int musicNo=0;
//	int modeNum = 0;
//	int lengthOfModes = modes.length;
//	int maxNumOFFiles = 0;
//	String pathn = null;
//	
//	
//	//Methods for Sound
//	public void mapSounds(String path){
//    	InputStream input1 = getClass().getResourceAsStream(path);
//    	try{
//    		path = path.substring(path.indexOf("/") + 1);
//    		path = path.substring(0, path.indexOf("."));
//    		path = path.replace("/",".");
//    		
//    			
//    		}catch(Exception e){
//    			e.printStackTrace();
//    		}	
//    	
//    	//sounds.put(path, getClass().getResourceAsStream(path));			//<<----NULL POINT EXCEPTION
//    	
//    }
//	public void playSound(String path) throws Exception {
//		InputStream input = getClass().getResourceAsStream(path);
//		this.player = new Player(input);
//		//this.player = new Player (sounds.get(path));			//----> IF HANDLED
//		
//		        synchronized (playerLock) {
//		            switch (playerStatus) {
//		                case NOTSTARTED:
//		                    final Runnable r = new Runnable() {
//		                        public void run() {
//		                            playInternalSound();
//		                        }
//		                    };
//		             
//		                    final Thread t = new Thread(r);
//		                    t.setDaemon(true);
//		                    t.setPriority(Thread.MAX_PRIORITY);
//		                    playerStatus = PLAYING;
//		                    t.start();
//		                    break;
//		                case PAUSED:
//		                    resume();
//		                    break;
//		                default:
//		                    break;
//		            }
//		        }
//	}
//	
//	
//	//Methods for Music
//	public void fillPlayOrder(){
//		System.out.println("Begin");
//		try{
//		
//		for(int i = 0 ; i < 4; i++){
//			int num = 0;	
//			String mode = modes[i];		
//			String name;				
//			java.net.URL s = this.getClass().getClassLoader().getResource("Music");
//			String a = s.getPath();
//			a = a.replace("bin", "src");
//			a= a.replace("%20", " ");
//			a= a+"/"+mode;
//			File f = new File(a);
//			int numOfFiles = 0;
//			numOfFiles  = f.list().length;		//count of music for each mode
//			maxNumOfFiles[i] = numOfFiles;
//			String [] names = f.list();				
//			for(int j=0; j<numOfFiles; j++) {		
//				name = names[j];					
//				playOrder[i][j] = num;				
//				num= num+1;
//				modeNamePath[i][j] = ("Music"+"/"+mode+"/"+name);		
//
//			}
//			
//		}
//		}catch (Exception e)
//	    {
//			e.printStackTrace();
//	    }
//	}
//	public void setMusicFolder(String path){
//		this.musicFolder = path;
//	}
//	public void getMap( String currentModeName) throws Exception{
//		this.currentModeName = currentModeName;
//		
//		for(int i = 0; i< lengthOfModes; i++){
//			if(modes[i] == currentModeName){
//				modeNum = i;
//				maxNumOFFiles = maxNumOfFiles[i];
//			}
//		}
//		currentMusic = indices[modeNum];
//		musicNo = playOrder[modeNum][currentMusic];
//		musicName = modeNamePath[modeNum][currentMusic];
//		InputStream input = getClass().getResourceAsStream(musicName);
//		this.player = new Player(input);
//		
//	}
//		
//	public void playMusic( String currentModeName) throws Exception {
//		this.currentModeName = currentModeName;
//			getMap(currentModeName);
//		        synchronized (playerLock) {
//		            switch (playerStatus) {
//		                case NOTSTARTED:
//		                    final Runnable r = new Runnable() {
//		                        public void run() {
//		                            playInternal();
//		                        }
//		                    };
//		             
//		                    final Thread t = new Thread(r);
//		                   
//		                    playerStatus = PLAYING;
//		                    t.start();
//		                    break;
//		                case PAUSED:
//		                    resume();
//		                    break;
//		                default:
//		                    break;
//		            }
//		        }
//	}
//	public boolean pause() {
//		synchronized (playerLock) {
//		   if (playerStatus == PLAYING) {
//			   playerStatus = PAUSED;
//		   }
//		   return playerStatus == PAUSED;
//		}
//	}
//	/**
//	* Resumes playback. Returns true if the new state is PLAYING.
//	*/
//	public boolean resume() {
//		synchronized (playerLock) {
//		   if (playerStatus == PAUSED) {
//		      playerStatus = PLAYING;
//		      playerLock.notifyAll();
//		   }
//		return playerStatus == PLAYING;
//		}
//	 }
//
//		    /**
//		     * Stops playback. If not playing, does nothing
//		     */
//		    public void stop() {
//		        synchronized (playerLock) {
//		            playerStatus = FINISHED;
//		            playerLock.notifyAll();
//		        }
//		    }
//		    private void playInternalSound() {
//		        while (playerStatus != FINISHED) {
//		            try {
//		                if (!player.play(1)) {
//		                    break;
//		                }
//		            } catch (final Exception e) {
//		                break;
//		            }
//		            // check if paused or terminated
//		            synchronized (playerLock) {
//		                while (playerStatus == PAUSED) {
//		                    try {
//		                        playerLock.wait();
//		                    } catch (final InterruptedException e) {
//		                        // terminate player
//		                        break;
//		                    }
//		                }
//		            }
//		        }
//		        close();
//		    }
//
//
//		    private void playInternal() {
//		        while (playerStatus != FINISHED) {
//		        	
//		            try {
//		                if (!player.play(1)) {
//		                	isFinished = true;
//		                	for(int i = 0; i< lengthOfModes; i++){
//		                		if(modes[i] == currentModeName){
//		                			modeNum = i;
//		                			maxNumOFFiles = maxNumOfFiles[i];
//		                		}
//		                	}
//		                	if (isFinished){
//		                		if (currentMusic == maxNumOFFiles-1){	//checks if finished turn to beginning
//		                			currentMusic =0; 
//		                		}
//		                		else{
//		                		currentMusic=currentMusic+1;
//		                		}
//		                		 musicNo = playOrder[modeNum][currentMusic];
//		                		 musicName = modeNamePath[modeNum][currentMusic];
//		                		 InputStream input = getClass().getResourceAsStream(musicName);
//		                		 this.player = new Player(input);
//		                		 
//		                	}
//		                	player.play();
//		                	 
//		    	        	
//		                }
//		            } catch (final Exception e) {
//		                break;
//		            }
//		            // check if paused or terminated
//		          
//		            synchronized (playerLock) {
//		                while (playerStatus == PAUSED) {
//		                    try {
//		                        playerLock.wait();
//		                    } catch (final InterruptedException e) {
//		                        // terminate player
//		                        break;
//		                    }
//		                }
//		            }
//		        }
//		        close();
//		    }
//
//		    /**
//		     * Closes the player, regardless of current state.
//		     */
//		    public void close() {
//		    	synchronized (playerLock) {
//		            playerStatus = FINISHED;
//		        }
//		        try {
//		            player.close();
//		        } catch (final Exception e) {
//		            // ignore, we are terminating anyway
//		        }
//		    }
//	
//}
