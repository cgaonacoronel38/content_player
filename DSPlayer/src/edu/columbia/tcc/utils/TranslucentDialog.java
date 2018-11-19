/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author tokio
 */
public class TranslucentDialog extends JPanel implements ActionListener {

    private static JFrame fContent;
    private static JFrame fEvent;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private final Date now = new Date();
    private final Timer timer = new Timer(1000, this);
    private final JLabel text = new JLabel();
    private static Float translucedValue = 0.75f;

    public TranslucentDialog() {
        super(true);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        now.setTime(System.currentTimeMillis());
        text.setText(String.format("<html><body><font size='50'>%s</font></body></html>", sdf.format(now)));
    }

    public void showDialogContent(String message) {
        Thread thread = new Thread() {
            public void run() {
                try {

                    fContent = new JFrame();
                    fContent.setUndecorated(true);
                    setTranslucency(fContent);
                    fContent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fContent.setBackground(new Color(0f, 0f, 0f, 1f));
                    JPanel p = new TranslucentDialog();
                    JLabel l = new JLabel(message);
                    l.setMaximumSize(new Dimension(200, 600));
                    l.setFont(new Font(l.getFont().getName(), Font.PLAIN, 40));
                    p.add(l);
                    fContent.add(p);
                    fContent.setMaximumSize(new Dimension(200, 600));
                    fContent.pack();
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                    Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
                    int x = (int) rect.getMaxX() - fContent.getWidth();
                    int y = (int) rect.getMaxY() - fContent.getHeight();
                    fContent.setLocation(x, y);
                    fContent.setVisible(true);

                    Thread.sleep(5000);
                    while (translucedValue >= 0.5f) {
                        translucedValue -= 0.5f;
                        setTranslucency(fContent);
                        Thread.sleep(100);
                    }
                    closeDialogContent();
//            System.exit(0);
                } catch (InterruptedException ex) {
                    closeDialogContent();
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(TranslucentDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        thread.start();
    }

    public void showDialogEvent(String message) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    fEvent = new JFrame();
                    fEvent.setUndecorated(true);
                    setTranslucency(fEvent);
                    fEvent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    fEvent.setBackground(new Color(0f, 0f, 0f, 1f));
                    JPanel p = new TranslucentDialog();
                    JLabel l = new JLabel(message);
                    l.setMaximumSize(new Dimension(200, 600));
                    l.setFont(new Font(l.getFont().getName(), Font.PLAIN, 40));
                    p.add(l);
                    fEvent.add(p);
                    fEvent.setMaximumSize(new Dimension(200, 600));
                    fEvent.pack();
                    fEvent.setLocationRelativeTo(null);
                    fEvent.setVisible(true);

                    Thread.sleep(2000);
                    while (translucedValue >= 0.5f) {
                        translucedValue -= 0.5f;
                        setTranslucency(fEvent);
                        Thread.sleep(100);
                    }
                    closeDialogEvent();
//            System.exit(0);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(TranslucentDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    closeDialogEvent();
                }
            }
        };
        thread.start();
    }

    // taken from: http://java.sun.com/developer/technicalArticles/GUI/translucent_shaped_windows/
    private static void setTranslucency(Window window) throws InvocationTargetException {
        try {
            Class<?> awtUtilitiesClass = Class.forName("com.sun.awt.AWTUtilities");
            Method mSetWindowOpacity = awtUtilitiesClass.getMethod("setWindowOpacity", Window.class, float.class);
            if (!mSetWindowOpacity.isAccessible()) {
                mSetWindowOpacity.setAccessible(true);
            }
            mSetWindowOpacity.invoke(null, window, translucedValue);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }

    }

    public void closeDialogContent() {
        fContent.dispose(); // close window
        fContent.setVisible(false);
    }

    public void closeDialogEvent() {
        fEvent.dispose(); // close window
        fEvent.setVisible(false);
    }
}
