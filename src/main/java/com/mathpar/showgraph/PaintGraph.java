/**
* Copyright © 2011 Mathparca Ltd. All rights reserved.
*/

package com.mathpar.showgraph;


/**
 *
 * @author Alex
 */
import com.mathpar.func.F;
import java.awt.*;
import java.awt.event.*;
import com.mathpar.number.Ring;

public class PaintGraph extends Frame implements WindowListener {

    public void windowClosing(WindowEvent we) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent we) {
    }

    public void windowDeactivated(WindowEvent we) {
    }

    public void windowActivated(WindowEvent we) {
    }

    public void windowIconified(WindowEvent we) {
    }

    public void windowDeiconified(WindowEvent we) {
    }

    public void windowOpened(WindowEvent we) {
    }

    public PaintGraph() {
        setSize(800, 600);
        addWindowListener(this);
        funcs = new String[5];
        dnum = new int[5];
        h = 0;
        lev = null;
    }
    String[] funcs;
    int[] dnum, lev;
    int h;

    public void init(F f) {
        dfs(0, f);
        int tmp = 0;
        for (int i = 0; i < dnum.length; i++) {
            if (dnum[i] > tmp) {
                tmp = dnum[i];
            }
        }
        h = tmp + 1;
        lev = new int[h];
        lev[0] = 1;
        tmp = 0;
        for (int i = 1; i < h; i++) {
            for (int j = 0; j < dnum.length; j++) {
                if (dnum[j] == i) {
                    tmp++;
                }
            }
            lev[i] = tmp;
            tmp = 0;
        }
        setTitle(f.toString());
        setVisible(true);
    }

    public void paint(Graphics g) {
        int[] levels = new int[lev.length];
        Dimension d = getSize();
        int x1, y1, x2, y2;
        g.drawString(funcs[0], d.width / 2, d.height / (h + 1));
        g.drawOval(d.width / 2 - 6, d.height / (h + 1) - 14, 15 + funcs[0].length() * 8, 20);
        levels[0]++;
        for (int i = 1; i < search(); i++) {
            levels[dnum[i]]++;
            x1 = levels[dnum[i]] * d.width / (lev[dnum[i]] + 1);
            y1 = (1 + dnum[i]) * d.height / (h + 1);
            x2 = levels[dnum[i] - 1] * d.width / (lev[dnum[i] - 1] + 1);
            y2 = (dnum[i]) * d.height / (h + 1);
            g.drawString(funcs[i], x1, y1);
            g.drawOval(x1 - 8, y1 - 15, 15 + funcs[i].length() * 8, 20);
            g.drawLine(x1 + 4 * funcs[i].length(), y1 - 16, x2 + 8, y2 + 5);
        }
    }

    void dfs(int dn, F f) {
        int ind = search();
        funcs[ind] = F.FUNC_NAMES[f.name];
        dnum[ind] = dn;
        for (int i = 0; i < f.X.length; i++) {
            if (f.X[i] instanceof F) {
                dfs(dn + 1, (F) f.X[i]);
            } else {
                ind = search();
                funcs[ind] = f.X[i].toString();
                dnum[ind] = dn + 1;
            }
        }
    }

    String[] resize(String[] m) {
        String[] a = new String[m.length + 5];
        System.arraycopy(m, 0, a, 0, m.length);
        return a;
    }

    int[] resize(int[] m) {
        int[] a = new int[m.length + 5];
        System.arraycopy(m, 0, a, 0, m.length);
        return a;
    }

    int search() {
        int i = funcs.length - 1;
        if (funcs[i] != null) {
            funcs = resize(funcs);
            dnum = resize(dnum);
            return i + 1;
        }
        for (; i >= 0; i--) {
            if (funcs[i] != null) {
                return i + 1;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        PaintGraph g = new PaintGraph();
        g.init(new F("\\exp(x^2+1)+\\tg(x+y+z)+\\sin(x+y+z)+\\cos(x+y+z)", new Ring("R[x]")));

    }
}
