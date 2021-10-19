/**
 * 
 */
package io.tatlook.lchaos.formula;

import io.tatlook.lchaos.ifs.FractintFileSaver;
import io.tatlook.lchaos.saver.AbstractFileSaver;

/**
 * @author YouZhe Zhen
 *
 */
public class MandelbrotSetSaver extends AbstractFileSaver<MandelbrotSetData> {

	@Override
	public void save() {
		out.println(FractintFileSaver.getFileNameNoEx(file.getName()) + " {");
		out.println(data.getJavaCode());
		out.println("}");
	}

}
