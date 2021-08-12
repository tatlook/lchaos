/**
 * 
 */
package io.tatlook.lchaos.saver;

import java.io.File;

import io.tatlook.lchaos.data.IFSData;

/**
 * 
 * 
 * @author YouZhe Zhen
 */
public abstract class IFSFileSaver extends AbstractFileSaver {

	protected IFSData data;

	public IFSFileSaver(File file, IFSData data) {
		super(file);
		this.data = data;
	}

}
