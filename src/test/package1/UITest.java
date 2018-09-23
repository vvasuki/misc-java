package package1;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class UITest extends JFrame{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(UITest.class.toString());
    static {
        logger.setLevel(Level.ALL);
    }
    KeyListener keyListener = new KeyListener() {

        public void keyTyped(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            // TODO Auto-generated method stub
            
        }

        public void keyPressed(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            if(keyEvent.getKeyChar()=='z')
            dlg.setVisible(true);
            else if(keyEvent.getKeyChar()=='\n') {
                logger.info(ot.getValue().toString());
                
            }
            
            // TODO Auto-generated method stub
            
        }

        public void keyReleased(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            // TODO Auto-generated method stub
            
        }
        
    };
    JOptionPaneTest ot= new JOptionPaneTest();
    
    JDialog dlg = ot.createDialog(UITest.this,"title");
    UITest() {

//        dlg.addKeyListener(keyListener);
//        ot.addKeyListener(keyListener);
//        ActionMap actionMap = ot.getActionMap();
//        logger.info(actionMap.toString());
//        logger.info(actionMap.keys().toString());
//        for(int i=0;i<actionMap.keys().length;i++) {
//            logger.info(actionMap.keys()[i].toString());
//            logger.info(actionMap.keys()[i].toString());
//        }
        Container windowComponentContainer = getContentPane();
        JButton btnYes = new JButton("yes");
        btnYes.addKeyListener(keyListener);
        JButton btnNo = new JButton("no");
        btnNo.addKeyListener(keyListener);
        windowComponentContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
        windowComponentContainer.add(btnYes);
        windowComponentContainer.add(btnNo);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addKeyListener(keyListener);
    }
    
    public static void main(String[] args) {
        UITest uiTest = new UITest();
        uiTest.setVisible(true);

    
    }

 
}
