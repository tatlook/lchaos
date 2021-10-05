/**
 * 
 */
package io.tatlook.lchaos.ifs;

import io.tatlook.lchaos.saver.AbstractFileSaver;

/**
 * 
 * 
 * @author YouZhe Zhen
 */
public abstract class IFSFileSaver extends AbstractFileSaver {

	protected IFSData data;

	public IFSFileSaver(IFSData data) {
		this.data = data;
	}

}
