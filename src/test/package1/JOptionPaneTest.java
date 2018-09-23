package package1;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class JOptionPaneTest extends JOptionPane {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(JOptionPaneTest.class
            .toString());
    static {
        logger.setLevel(Level.ALL);
    }

    private class OpKeyListener implements KeyListener{
        ActionListener actListener;
        JButton button; 

        public void keyTyped(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            // TODO Auto-generated method stub
            if(keyEvent.getKeyChar() == '\n'){
                actListener.actionPerformed(new ActionEvent(button, ActionEvent.ACTION_PERFORMED, null));
            }
            
        }

        public void keyPressed(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            // TODO Auto-generated method stub
            
        }

        public void keyReleased(KeyEvent keyEvent) {
            logger.info(keyEvent.toString());
            // TODO Auto-generated method stub
            
        }
        
    }

    private void parseButtons(Container x) {
        // logger.info(x.toString());
        for (int i = 0; i < x.getComponentCount(); i++) {
            Component comp = x.getComponent(i);
            if (comp instanceof JButton) {
                JButton buttonComp = (JButton) comp;
                logger.info(buttonComp.toString());

              ActionMap actionMap = buttonComp.getActionMap();
              logger.info(actionMap.toString());
//              if (actionMap.keys() == null) {
//                  logger.info("actionMap keys are null.");
//              }
//              else {
//                  logger.info(actionMap.keys().toString());
//
//                  for (int j = 0; j < actionMap.keys().length; j++) {
//                      logger.info(actionMap.keys()[j].toString());
//                      logger.info(actionMap.keys()[j].toString());
//                  }
//              }


                ActionListener[] actListeners = buttonComp.getActionListeners();
                logger.info("actListeners.length: "+actListeners.length);
                logger.info("actListeners[0]: "+actListeners[0]);
                
                OpKeyListener opKeyListener = new OpKeyListener();
                opKeyListener.actListener = actListeners[0];
                opKeyListener.button = buttonComp;

                
//                KeyListener[] keyListeners = buttonComp.getKeyListeners();
//                logger.info("keyListeners.length: "+keyListeners.length);
                
                buttonComp.addKeyListener(opKeyListener);

                ActionListener act = buttonComp.getActionForKeyStroke(KeyStroke
                        .getKeyStroke(' '));
                logger.info("act is " + act);


            }
            if (comp instanceof Container) {
                Container containerComp = (Container) comp;
                parseButtons(containerComp);

            }
        }

    }

    public JOptionPaneTest() {
        super("ole", QUESTION_MESSAGE, YES_NO_OPTION, null, new String[] {
                "ya", "neing" });
        parseButtons(this);

        logger.info("ole");
        // TODO Auto-generated constructor stub
    }

}
