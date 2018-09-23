/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package package1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vvasuki
 */

interface TmpList{
    void add(Object x);
    int size();
}

class Node{
    Object obj;
    Node nextNode;
}



class LinkedList{
    Node lastNode = null;
    int size = 0;
    int getSize(){
        return size;
    }
    void add(Object x){
        if(size == 0)
            lastNode = new Node();
        else
            lastNode = lastNode.nextNode;
        lastNode.obj = x;
        size++;
    }

    void getWords(String prefix, String remNum, Map numToCharsMap){
        List lstChars = (List)numToCharsMap.get(remNum.substring(0, 0));
        if(remNum.length() == 1){
            for(int i = 0; i < lstChars.size(); i++)
                System.out.println(prefix + (String)lstChars.get(i));
            return;
        }
        for(int i = 0; i < lstChars.size(); i++) {
            this.getWords(prefix + (String)lstChars.get(i), remNum.substring(1, remNum.length()-1), numToCharsMap);
        }
    }

}

public class AmazonScreen {

    public static void main(String[] args){
        List<Integer> lstTmp = new ArrayList<Integer>();
        
    }
}
