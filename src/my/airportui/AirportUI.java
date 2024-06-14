/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

//GongEthanAirport
//By: Ethan Gong
//Fun fact: I drew the animations myself.

package my.airportui;

//Imports:
import java.io.FileNotFoundException;
import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author chesp
 */
public class AirportUI extends javax.swing.JFrame {
    
    //Variables to control the landing animation of the airplane.
    ImageIcon landingImage1 = loadImage("Landing Frame 1.png");
    ImageIcon landingImage2 = loadImage("Landing Frame 2.png");
    ImageIcon landingImage3 = loadImage("Landing Frame 3.png");
    ImageIcon landingImage4 = loadImage("Landing Frame 4.png");
    ImageIcon landingImage5 = loadImage("Landing Frame 5.png");
    ImageIcon landingImage6 = loadImage("Landing Frame 6.png");
    private int landingDrawingCounter = 1;
    private boolean drawLanding = false;
    
    //Variables to control the take off animation of the airplane.
    ImageIcon takeOffImage1 = loadImage("Take Off Frame 1.png");
    ImageIcon takeOffImage2 = loadImage("Take Off Frame 2.png");
    ImageIcon takeOffImage3 = loadImage("Take Off Frame 3.png");
    ImageIcon takeOffImage4 = loadImage("Take Off Frame 4.png");
    ImageIcon takeOffImage5 = loadImage("Take Off Frame 5.png");
    ImageIcon takeOffImage6 = loadImage("Take Off Frame 6.png");
    private int takeOffDrawingCounter = 1;
    private boolean drawTakeOff = false;

    //Variables for the original files with the starting data inside.
    private File arrivals = new File("arrivals.txt");
    private File takeoffs = new File("takeoffs.txt");
    
    //The only global variables in the project, the two queues.
    //One queue for arrivals and one queue for take offs.
    public static Queue<Integer> landing = new LinkedList<Integer>();
    public static Queue<Integer> takeOff = new LinkedList<Integer>();
    
    //Two timer variables.
    //t1 is set to 600 milliseconds with tDuration as specified.
    //t2 is set to 100 milliseconds for the 50x50 animation.
    Timer t1;
    Timer t2;
    
    //tDuration controls the duration of t1 by milliseconds.
    private int tDuration = 600;
    
    //cycleCount counts the amount of cycles the program has run through.
    private int cycleCount = 0;
    //arrivalCount counts the amount of arrivals.
    private int arrivalCount = 0;

    //This variable controls whether or not the airplane has the green light to take off.
    private boolean takeOffOkay = false;

    //These variables are for the updateQueue method, controlling what happens in it.
    private boolean removeTakeOff = false;
    private boolean removeLanding = false;
    private boolean addTakeOff = false;
    private boolean addLanding = false;

    //These variables hold the values added to the queues.
    private int addLandingValue = 0;
    private int addTakeOffValue = 0;
    
    //These variables set the jLabel that shows the user what's going on.
    private int flight = 0;
    private int landingCounter = 5;
    private int takeOffCounter = 3;
    
    //This variable controls the text areas.
    private boolean landingText = true;
    //This variable will tell whether the program has started or not.
    private boolean running = false;
    
    /**
     * Creates new form AirportUI
     */
    public AirportUI() {
        //This sets up the code for when the program runs.
        initComponents();
        jLabel3.setIcon(landingImage1);

        t1 = new Timer(tDuration, new AirportUI.TimerListener());
        t2 = new Timer(100, new AirportUI.TimerListener2());
        //File input output for the two files that have the starting data stored.
        try {
            Scanner s = new Scanner(arrivals);
            while (s.hasNextInt()) {
                int element = s.nextInt();
                landing.add(element);
            }
        } catch (FileNotFoundException ex) {
        } catch (NoSuchElementException ex) {
        }

        try {
            Scanner s = new Scanner(takeoffs);
            while (s.hasNextInt()) {
                int element = s.nextInt();
                takeOff.add(element);
            }
        } catch (FileNotFoundException ex) {
        } catch (NoSuchElementException ex) {
        }
        
        //This will display the starting data in the text areas.
        landingText = true;
            updateQueue(landing);
            landingText = false;
            updateQueue(takeOff);
    }

    //This is the TimerListener that will run every 600 milliseconds.
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //Counting the cycles.
            cycleCount++;
            //Updates the text areas.
            landingText = true;
            updateQueue(landing);
            landingText = false;
            updateQueue(takeOff);
            //This sets the jLabel that shows the user what's going on.
            if(!takeOffOkay && !landing.isEmpty()) {
                landingCounter--;
                if(landingCounter==0) {
                    landingCounter = 4;
                }
                flight = landing.peek();
                jLabel2.setText("Flight "+flight+" is next to land. "+landingCounter);
            } else if(takeOffOkay && !takeOff.isEmpty()){
                takeOffCounter--;
                if(takeOffCounter==0) {
                    takeOffCounter = 2;
                }
                flight = takeOff.peek();
                jLabel2.setText("Flight "+flight+" is next to take off. "+takeOffCounter);
            }
            //If both queues aren't empty, then this will run.
            if (!landing.isEmpty() && !takeOff.isEmpty()) {
                if (cycleCount % 4 == 0) {
                    drawLanding = true;
                    arrivalCount++;
                    removeLanding = true;
                    landingText = true;
                    updateQueue(landing);
                    if (arrivalCount >= 2) {
                        takeOffOkay = true;
                    }
                } else if (cycleCount % 2 == 0 && takeOffOkay) {
                    drawTakeOff = true;
                    removeTakeOff = true;
                    landingText = false;
                    updateQueue(takeOff);
                    cycleCount = 0;
                    arrivalCount = 0;
                    takeOffOkay = false;
                }
                //If only the landing queue is empty, this will run.
            } else if (landing.isEmpty() && !takeOff.isEmpty()) {
                if(cycleCount == 1||cycleCount == 9||cycleCount == 5) {
                    jLabel2.setText("Flight "+flight+" is next to take off. "+2);
                } else if(cycleCount == 2||cycleCount == 10||cycleCount == 6) {
                    jLabel2.setText("Flight "+flight+" is next to take off. "+1);
                }
                if (cycleCount % 2 == 0) {
                    drawTakeOff = true;
                    removeTakeOff = true;
                    landingText = false;
                    updateQueue(takeOff);
                    cycleCount = 0;
                }
                //If only the take off queue is empty, this will run.
            } else if (!landing.isEmpty() && takeOff.isEmpty()) {
                if (cycleCount % 4 == 0) {
                    drawLanding = true;
                    removeLanding = true;
                    landingText = true;
                    updateQueue(landing);
                    cycleCount = 0;
                }
                //If both queues are empty, this will run.
            } else if(landing.isEmpty()&&takeOff.isEmpty()) {
                cycleCount = 0;
                jLabel2.setText("Waiting for input.");
            }
        }
    }
    
    //This TimerListener2 class runs every 100 milliseconds to display the animations.
    private class TimerListener2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            //If it's time to animate the landing, then this runs.
            if(drawLanding) {
                if(landingDrawingCounter==7) {
                    landingDrawingCounter = 1;
                }
                switch(landingDrawingCounter) {
                    case 1:
                        jLabel3.setIcon(landingImage1);
                        break;
                    case 2:
                        jLabel3.setIcon(landingImage2);
                        break;
                    case 3:
                        jLabel3.setIcon(landingImage3);
                        break;
                    case 4:
                        jLabel3.setIcon(landingImage4);
                        break;
                    case 5:
                        jLabel3.setIcon(landingImage5);
                        break;
                    case 6:
                        jLabel3.setIcon(landingImage6);
                        drawLanding = false;
                        break;
                }
                landingDrawingCounter++;
                //If it's time to animate the take off, this runs.
            } else if (drawTakeOff) {
                if(takeOffDrawingCounter==7) {
                    takeOffDrawingCounter = 1;
                }
                switch(takeOffDrawingCounter) {
                    case 1:
                        jLabel3.setIcon(takeOffImage1);
                        break;
                    case 2:
                        jLabel3.setIcon(takeOffImage2);
                        break;
                    case 3:
                        jLabel3.setIcon(takeOffImage3);
                        break;
                    case 4:
                        jLabel3.setIcon(takeOffImage4);
                        break;
                    case 5:
                        jLabel3.setIcon(takeOffImage5);
                        break;
                    case 6:
                        jLabel3.setIcon(takeOffImage6);
                        drawTakeOff = false;
                        break;
                }
                takeOffDrawingCounter++;
            }
        }
    }

    //This is the update queue method.
    private Queue<Integer> updateQueue(Queue<Integer> inputQueue) {
        //This controls what goes in and out of queues.
        if (removeTakeOff) {
            inputQueue.remove();
            removeTakeOff = false;
        } else if (removeLanding) {
            inputQueue.remove();
            removeLanding = false;
        } else if (addLanding) {
            inputQueue.add(addLandingValue);
            addLanding = false;
        } else if (addTakeOff) {
            inputQueue.add(addTakeOffValue);
            addTakeOff = false;
        }
        
        //This bit will display the queue in the text areas.
        Queue<Integer> tempQueue = new LinkedList<>();

        String text = "";

        //Print and re-add each element.
        while (!inputQueue.isEmpty()) {
            int element = inputQueue.poll();
            text += element + "\n";
            tempQueue.add(element);
        }
        
        if (landingText) {
            jTextArea1.setText(text);
        } else {
            jTextArea2.setText(text);
        }
        
        while (!tempQueue.isEmpty()) {
            inputQueue.add(tempQueue.poll());
        }
        
        return inputQueue;
    }
    
    //This is the loadImage method, to set the images at the start to be able to be animated.
    private static ImageIcon loadImage(String filePath) {
        try {
            File file = new File(filePath);
            Image image = ImageIO.read(file);
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawingArea1 = new my.airportui.DrawingArea();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        drawingArea1.setBorder(javax.swing.BorderFactory.createTitledBorder("Airport Animation"));

        jLabel3.setText("50 x 50 Animation");

        javax.swing.GroupLayout drawingArea1Layout = new javax.swing.GroupLayout(drawingArea1);
        drawingArea1.setLayout(drawingArea1Layout);
        drawingArea1Layout.setHorizontalGroup(
            drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingArea1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(287, Short.MAX_VALUE))
        );
        drawingArea1Layout.setVerticalGroup(
            drawingArea1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(drawingArea1Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 61, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Airport Simulator"));

        jButton1.setText("Exit");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Start");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setFocusable(false);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea2);

        jTextField1.setNextFocusableComponent(jTextField2);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jTextField2.setNextFocusableComponent(jTextField1);
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField2KeyPressed(evt);
            }
        });

        jLabel1.setText("No Input Errors");

        jLabel2.setText("Press 'Start' to begin simulation.");

        jLabel4.setText("Arrivals:");

        jLabel5.setText("Take Offs:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton2)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(76, 76, 76)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(84, 84, 84))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(drawingArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(drawingArea1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //This is the exit button.
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //This runs when the start button is pressed, starting both timers.
        if(!running) {
            t1.start();
            t2.start();
        }
        running = true;
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyPressed
        //This is the take off text field.
        if (running) {
            //When the enter key is pressed,this runs.
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                String input = jTextField2.getText();
                //Input verification.
                if (input == null || input.isEmpty()) {
                    jLabel1.setText("Please don't leave the text field empty.");
                } else {
                    try {
                        int val = Integer.parseInt(input);
                        jLabel1.setText("Input verified.");
                        addTakeOffValue = val;
                        addTakeOff = true;
                        landingText = false;
                        updateQueue(takeOff);
                        jTextField2.setText("");
                    } catch (NumberFormatException e) {
                        jLabel1.setText("Please enter only integers.");
                        jTextField2.setText("");
                    }
                }
            }
        } else {
            jLabel1.setText("Please start the program first.");
        }
    }//GEN-LAST:event_jTextField2KeyPressed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        //This is the landing text field.
        if (running) {
            //When the enter key is pressed,this runs.
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                String input = jTextField1.getText();
                //Input verification.
                if (input == null || input.isEmpty()) {
                    jLabel1.setText("Please don't leave the text field empty.");
                } else {
                    try {
                        int val = Integer.parseInt(input);
                        jLabel1.setText("Input verified.");
                        addLandingValue = val;
                        addLanding = true;
                        landingText = true;
                        updateQueue(landing);
                        jTextField1.setText("");
                    } catch (NumberFormatException e) {
                        jLabel1.setText("Please enter only integers.");
                        jTextField1.setText("");
                    }
                }
            }
        } else {
            jLabel1.setText("Please start the program first.");
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AirportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AirportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AirportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AirportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirportUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private my.airportui.DrawingArea drawingArea1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
