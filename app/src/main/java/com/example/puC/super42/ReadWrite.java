package com.example.puC.super42;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Mark on 5-6-2016.
 */
public class ReadWrite {
    private static Context c;

    public ReadWrite(Context c) {
        this.c = c;
    }
    /**
     * Writes a highscore
     *
     * @param score : the highscore to save
     */
    public void saveScore(String score) {
        Log.d("saveScore", "started");
        File dir  = new File(c.getApplicationInfo().dataDir);
        dir.mkdirs();
        File myFile = new File(dir, "highscores.txt");
        try {
            //Log.d("saveScore", "s=" + score + " trying.. sd=" + dir.toString());
            if (!myFile.exists()) {
                try {
                    myFile.createNewFile();
                } catch (Exception e) {
                    Log.d("saveScore", "myFile.createNewFile() failed " + e.toString());
                }
            }
            FileOutputStream out = new FileOutputStream(myFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            String append = score + " " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            if (Pattern.matches("\\A\\d+\\s\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", append)) {
                writer.append("\n" + append);
            }
            writer.close();
            out.close();
            //Log.d("saveScore", "succes");
        } catch (Exception e) {
            Log.d("saveScore", "Exception " + e.toString());
        }
        readHighscores();
    }


    /**
     * Reads all the highscores and sorts them
     * @return : a sorted list of scores witht the dates
     */
    public ArrayList<String> readHighscores() {
        Log.d("readHighscores", "started");
        File dir  = new File(c.getApplicationInfo().dataDir);
        File file = new File(dir, "highscores.txt");
        ArrayList<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            int i = countLines("highscores.txt");
            //Log.d("readHighscores", "i=" + i);
            while ((s = br.readLine()) != null) {
                if (Pattern.matches("\\A\\d+\\s\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", s)) {
                    res.add(s);
                }
            }
            br.close();
        } catch (IOException e) {
            Log.d("readHighscores", e.toString());
        }
        //Log.d("readHighscores", "res=" + res + " sorted=" + bubblesortScore(res));
        res = bubblesortScore(res);
        for (int i=0; i< res.size(); i++) {
            res.set(i, (i+1) + (i < 9 ? ".   " : ". ") + res.get(i));
        }
        return res;
    }



    /**
     * Writes a challenge
     * @param challange : the challenge to save
     */
    public void saveChallange(String challange) {
        if (MainActivity.challengesCompleted.contains(challange))
            return;
        else
            MainActivity.challengesCompleted.add(challange);

        //Log.d("saveChallange", "started challenge=" + challange);
        File dir  = new File(c.getApplicationInfo().dataDir);
        dir.mkdir();
        File myFile = new File(dir, "challenges.txt");
        try {
            if (!myFile.exists()) {
                try {
                    myFile.createNewFile();
                } catch (Exception e) {
                    Log.d("saveScore", "myFile.createNewFile() failed " + e.toString());
                }
            }
            FileOutputStream out = new FileOutputStream(myFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            String append = challange + " " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            if (true || Pattern.matches("\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", append)) {
                Log.d("saveChallange", "appending");
                writer.append("\n" + append);
            }
            writer.close();
            out.close();
            //Log.d("saveChallange", "succes");
        } catch (Exception e) {
            Log.d("saveChallange", "Exception " + e.toString());
        }
    }


    /**
     * Reads all the highscores and sorts them
     * @return : a sorted list of scores witht the dates
     */
    public ArrayList<String> readChallenges() {
        File dir  = new File(c.getApplicationInfo().dataDir);
        File file = new File(dir, "challenges.txt");
        ArrayList<String> res = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                if (true || Pattern.matches("\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\Z", s)) {
                    res.add(s);
                }
            }
            br.close();
        } catch (IOException e) {
            Log.d("Read challenges", e.toString());
        }
        return res;
    }


    /**
     * A bubble sort implementation for the scores
     * @param list : the list to sort
     * @return : the sorted list
     */
    private static ArrayList<String> bubblesortScore (ArrayList<String> list) {
        boolean swap = true;
        Pattern p = Pattern.compile("\\A\\d+");

        while(swap){

            swap = false;
            // Swaps s0 en s1 iff s0 < s1
            for(int i=0; i<list.size()-1; i++){
                String s0 = list.get(i);
                String s1 = list.get(i+1);
                Matcher m0 = p.matcher(s0);
                Matcher m1 = p.matcher(s1);
                if (!m0.find() || !m1.find()) {
                    throw new IllegalArgumentException("Illegal list items");
                }
                //Log.d("bublesort", "s0=" + s0 + " s1=" + s1);
                if(Integer.parseInt(m0.group(0)) < Integer.parseInt(m1.group(0))){
                    list.set(i, s1);
                    list.set(i+1, s0);
                    swap = true;
                }
            }
        }
        return list;
    }



    /**
     * @param filename : the filename of the file to read
     * @return : the number of lines
     * @throws IOException : an exception that can be thrown
     */
    public static int countLines(String filename) throws IOException {
        File dir  = new File(c.getApplicationInfo().dataDir);
        File file = new File(dir, filename);
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    /**
     * Resets all the challenges by clearing the challenges.txt file
     */
    public static void resetChallenges()  {
        File dir  = new File(c.getApplicationInfo().dataDir);
        File file = new File(dir, "challenges.txt");
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }catch (IOException ie) {
            ie.printStackTrace();
        }

}
}
