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
	 * If and only if class is same, this method returns {@code true}.
	 * <br/>Because NullData cannot be edited.
	 * 
	 * @return is class same
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			return true;
		}
		return false;
	}

}
