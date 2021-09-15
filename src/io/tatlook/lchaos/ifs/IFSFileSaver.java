/**
 * 
 */
package io.tatlook.lchaos.ifs;

import java.io.File;

import io.tatlook.lchaos.saver.AbstractFileSaver;

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
