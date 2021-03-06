// Copyright 2002, FreeHEP.
package org.freehep.graphicsio.emf.gdi;

import java.io.IOException;

import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFTag;

/**
 * CloseFigure TAG.
 * 
 * @author Mark Donszelmann
 * @version $Id: CloseFigure.java,v 1.5 2009-08-17 21:44:44 murkle Exp $
 */
public class CloseFigure extends EMFTag {

	public CloseFigure() {
		super(61, 1);
	}

	@Override
	public EMFTag read(int tagID, EMFInputStream emf, int len)
			throws IOException {

		return this;
	}

}
