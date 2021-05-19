/**
 * 
 */
package io.tatlook.chaos;

/**
 * @author Administrator
 *
 */
public class ChaosFileDataException extends Exception {
	private static final long serialVersionUID = 7983856589835980238L;
	
	public static String DIST_ERROR = "Error in the data \"DIST\"";
	public static String CX_CY_ERROR = "Error in the data \"CX\" \"or CY\"";
	
	public ChaosFileDataException(String message) {
		super(message);
	}

	public ChaosFileDataException() {
	}
	
	public void openDialog() {
		ErrorMessageDialog.createChaosFileDataExceptionDialog(this);
	}
}
