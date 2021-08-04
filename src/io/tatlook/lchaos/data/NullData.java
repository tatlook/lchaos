/**
 * 
 */
package io.tatlook.lchaos.data;

/**
 * Null object of AbstractData.
 * 
 * @author YouZhe Zhen
 */
public class NullData extends AbstractData {

	public NullData() {
		super(null);
	}

	/**
	 * NullData cannot be edited, so the origin data is meaningless.
	 * This method doesn't do anything.
	 */
	@Override
	public void setCurrentToOrigin() {
	}

	/**
	 * NullData cannot be edited, so true must be returned.
	 * 
	 * @return true
	 */
	@Override
	protected boolean equalsToOrigin() {
		return true;
	}

}
