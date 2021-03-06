
package com.ai;

import java.util.ArrayList;
import javax.swing.JTextPane;

/**
 *
 * @author De Guzman & Mahinay
 */
public class BackChain {

    public static String tell;
    public static String ask;
    public static ArrayList<String> agenda;
    public static ArrayList<String> facts;
    public static ArrayList<String> clauses;
    public static ArrayList<String> entailed;

    public BackChain(String a, String t) {
        agenda = new ArrayList<>();
        clauses = new ArrayList<>();
        entailed = new ArrayList<>();
        facts = new ArrayList<>();
        tell = t;
        ask = a;
        init(tell);
    }

    public static void init(String tell) {
        agenda.add(ask);
        String[] rules = tell.split(";");
        for (String rule : rules) {
            if (!rule.contains("=>")) {
                facts.add(rule);
            } else {
                clauses.add(rule);
            }
        }
    }

    public String start(JTextPane textPane) {
        String output;
        if (bcentails(textPane)) {
            //output = "YES: ";
            output = "YES";
        } else {
            output = "NO";
        }
        return output;
    }

    public boolean bcentails(JTextPane textPane) {
        ArrayList<String> p = new ArrayList<>();
        while (!agenda.isEmpty()) {
            //get conclusion
            String q = agenda.remove(agenda.size() - 1);

            entailed.add(q);

            if (!facts.contains(q)) {
                for (int i = 0; i < clauses.size(); i++) {
                    if (conclusionContains(clauses.get(i), q)) {
                        //get premises
                        ArrayList<String> temp = getPremises(clauses.get(i));
                        for (int j = 0; j < temp.size(); j++) {
                            p.add(temp.get(j));
                        }
                    }
                }

            }

            if (p.isEmpty()) {
                int random = (int) (Math.random() * 3 + 1);
                Bot bot = new Bot();
                System.out.println("learning here...");
                if (random == 1) {
                    bot.botChat("I didn't understand your question", textPane);
                } else if (random >= 2) {
                    bot.botChat("I can't comprehend", textPane);
                }
                return false;
            } else {
                for (int i = 0; i < p.size(); i++) {
                    if (!entailed.contains(p.get(i))) {
                        agenda.add(p.get(i));
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<String> getPremises(String clause) {
        // get the premise
        String premise = clause.split("=>")[0];
        ArrayList<String> temp = new ArrayList<>();
        String[] conjuncts = premise.split("&");
        for (String conjunct : conjuncts) {
            if (!agenda.contains(conjunct)) {
                temp.add(conjunct);
            }
        }
        return temp;
    }

    public static boolean conclusionContains(String clause, String c) {
        String conclusion = clause.split("=>")[1];
        
        return conclusion.equals(c);
    }

}
