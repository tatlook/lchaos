/**
 * 
 */
package io.tatlook.lchaos.data;

/**
 * @author Administrator
 *
 */
public class NullData extends AbstractData {

	public NullData() {
		super(null);
	}

	@Override
	public void setCurrentToOrigin() {
	}

	@Override
	protected boolean equalsToOrigin() {
		return true;
	}

}
