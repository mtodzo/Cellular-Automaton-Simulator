package setupGUI;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class LoadGridException extends Exception {

	/**
	 * default for serialization
	 */
	private static final long serialVersionUID = 1L;
	

	LoadGridException(String message, Object ... values){
		super(String.format(message, values));
		JOptionPane.showMessageDialog(new JFrame(),
			    message,
			    "Inane warning",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	LoadGridException(Throwable cause, String message, Object ...values){
		super(String.format(message, values), cause);
		JOptionPane.showMessageDialog(new JFrame(),
			    message,
			    "Inane warning",
			    JOptionPane.WARNING_MESSAGE);
	}
	
	
	LoadGridException(Throwable cause) {
		super(cause);
	}
}
