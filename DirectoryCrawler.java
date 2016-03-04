import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Created by MMA on 3/4/2016.
 */

public abstract class DirectoryCrawler implements Runnable{

    private volatile File param1;
    private volatile String param2;


    static List<IndexRecord> list = new ArrayList<IndexRecord>();
    static List<StringRecord> Stringlist = new ArrayList<StringRecord>();

    public DirectoryCrawler(File param1, String param2)
    {
        this.param1 = param1;
        this.param2 = param2;
    }

    public static void main(String[] args) throws IOException {

    	/*Take directory from the user*/
        Scanner console = new Scanner(System.in);
        System.out.print("Enter dir: ");
        String directoryName = console.nextLine();

        /*Creates a File object to search in the file structure*/
        File f = new File(directoryName);

        /*the Crawl Function takes the File argument and starts the searching*/
        System.out.println("Populating indexes...");
        crawl(f);

        System.out.println("Number of files found: " + (list.size() - 1));
        //System.out.println("Select the Following Options: ");
        System.out.println("1. Search by file name");
        System.out.println("2. Search by file content(only for .txt)");

        String Option = console.nextLine();

        if(Option.compareTo("1") == 0)
        {
            System.out.println("File to search? (Include name with extension): ");
            String fileName = console.nextLine();
            searchList(fileName);
        }
        if(Option.compareTo("2") == 0)
        {
            System.out.println("Enter Text Content?: ");
            String TxtContent = console.nextLine();
            searchStringlist(TxtContent);
        }

        //debuglist();
    }




    public static void crawl(File f) throws IOException {
        run(f, "");
    }


    public static  void run(File f, String indent) throws IOException {

        IndexRecord temp = null;
        temp = new IndexRecord();
        temp._fname = f.getName();
        temp._fpath = f.getAbsolutePath();
        temp._fsize = (int) ((f.length())/1024);

        if(f.getName().endsWith(".txt") && f.isFile())
        {
            int readlimiter = 0;
            //System.out.println(f.getName() + " is a txt file");
            FileInputStream in = new FileInputStream(f.getAbsoluteFile());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String strLine;

            StringRecord temp2 = null;
            temp2 = new StringRecord();
            temp2._fname = f.getName();
            temp2._fpath = f.getPath();
            temp2._fstring = "";
            while ((strLine = br.readLine()) != null) {
                readlimiter++;
                temp2._fstring = temp2._fstring + strLine;
                if(readlimiter > 200)
                    break;
            }
            in.close();
            Stringlist.add(temp2);
        }


        list.add(temp);

        if (f.isDirectory()) {
            // recursive case: directory
            // print everything in the directory
            File[] subFiles = f.listFiles();
            indent += "    ";
            DirectoryCrawler[] myRunnable = new DirectoryCrawler[subFiles.length];

            for (int i = 0; i < subFiles.length; i++) {
                run(subFiles[i], indent);
            }
        }
    }

    private static void debuglist()
    {
        System.out.println("Debugging the LIST\n");
        for (ListIterator<IndexRecord> iter = list.listIterator(); iter.hasNext(); ) {
            IndexRecord element = iter.next();
            System.out.println(element._fsize + "\n");
        }
    }


    private static void searchStringlist(String TxtContent)
    {
        int counter = 0;
        System.out.println("Debugging the STRING LIST\n");
        for (ListIterator<StringRecord> iter = Stringlist.listIterator(); iter.hasNext(); ) {
            StringRecord element = iter.next();
            if(element._fstring.contains(TxtContent))
            {
                System.out.println("Text Found!");
                System.out.println("At File: " + element._fpath);
                counter = 1;
                break;
            }
        }

        if (counter == 0)
            System.out.println("Could not find the specified text!");
        //System.out.println(Stringlist.get(4));

    }
    private static void searchList(String fileName)
    {
        //System.out.println("Debugging the LIST\n");
        boolean successfulFind = false;
        for (ListIterator<IndexRecord> iter = list.listIterator(); iter.hasNext(); ) {
            IndexRecord element = iter.next();
            if(element._fname.compareTo(fileName) == 0)
            {
                System.out.println("File Found!");
                System.out.println("at: " + element._fpath);
                successfulFind = true;
            }
        }
        if(!successfulFind)
        {
            System.out.println("File does not exist with this name.");
        }
    }
}