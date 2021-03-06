package controlP5;

/**
 * controlP5 is a processing gui library.
 *
 *  2007-2010 by Andreas Schlegel
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author 		Andreas Schlegel (http://www.sojamo.de)
 * @modified	##date##
 * @version		##version##
 *
 */

import processing.core.PApplet;

/**
 * use a controlWindowCanvas to draw your own content into a control window.
 * 
 * @example ControlP5groupCanvas
 * 
 */
public abstract class ControlCanvas {

	public final static int PRE = 0;
	public final static int POST = 1;
	protected int _myMode = PRE;
	
	public ControlCanvas() {}
	
	public void setup(PApplet theApplet) {}
	
	/**
	 * controlCanvas is an abstract class and therefore needs to be extended by
	 * your class. draw(PApplet theApplet) is the only method that needs to be
	 * overwritten.
	 */
	public abstract void draw(PApplet theApplet);

	/**
	 * get the drawing mode of a ControlWindowCanvas. this can be PRE or POST.
	 * 
	 * @return
	 */
	public final int mode() {
		return _myMode;
	}

	/**
	 * set the drawing mode to PRE. PRE is the default.
	 */
	public final void pre() {
		setMode(PRE);
	}

	/**
	 * set the drawing mode to POST.
	 */
	public final void post() {
		setMode(POST);
	}

	/**
	 * 
	 * @param theMode
	 */
	public final void setMode(int theMode) {
		if (theMode == PRE) {
			_myMode = PRE;
		} else {
			_myMode = POST;
		}
	}

}
