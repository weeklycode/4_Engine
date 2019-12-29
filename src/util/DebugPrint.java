package util;

public class DebugPrint {
	private static int level = 0;
	private static boolean silent = false;
	private final static char[] levelOrganizer = {'*','|','-','~'};

	public synchronized static void addLvl(){
		if (!silent){
			level++;
		}
	}
	public synchronized static void subLvl(){
		if (!silent){
			level--;
		}
	}

	public synchronized static int getLevel(){
		return level;
	}
	public synchronized static void setLevel(int lvl){
		if (!silent){
			level = lvl;
		}
	}

	public synchronized static void spaces(){
		if (!silent){
			for (int x = 0; x<level; x++){
				System.out.print("  ");
			}
			if (level>0){//this and the line below make it so zero doesn't have a bullet point
				System.out.print(levelOrganizer[(level-1)%levelOrganizer.length]);
			}else if (level<0){
				System.err.println("ERROR: DebugPrint level is less than 0");
			}
		}
	}

	public synchronized static void println(String message){
		if (!silent){
			spaces();
			System.out.println(message);
		}
	}

	public synchronized static void printlnAdd(String message){
		if (!silent){
			println(message);
			addLvl();
		}
	}

	public synchronized static void print(String message){
		if (!silent){
			spaces();
			System.out.print(message);
		}
	}

	public synchronized static void errln(String message){
		if (!silent){
			spaces();
			System.err.println(message);
		}
	}

	public synchronized static void err(String message){
		if (!silent){
			spaces();
			System.out.print(message);
		}
	}

	public synchronized static void addEmptyLine(){
		if (!silent){
			System.out.println();
		}
	}

	public synchronized static void addEmptyLines(int amount){
		if (!silent){
			for (int x=0; x<amount; x++){
				addEmptyLine();
			}
		}
	}

	public synchronized static void setSilent(boolean b){
		silent = b;
	}
}
