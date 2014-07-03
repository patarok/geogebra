/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

*/
package geogebra.common.main;

import geogebra.common.awt.GPoint;
import geogebra.common.euclidian.EuclidianView;
import geogebra.common.euclidian.EuclidianViewInterfaceCommon;
import geogebra.common.euclidian.event.AbstractEvent;
import geogebra.common.gui.Layout;
import geogebra.common.gui.view.consprotocol.ConstructionProtocolNavigation;
import geogebra.common.gui.view.consprotocol.ConstructionProtocolView;
import geogebra.common.kernel.ModeSetter;
import geogebra.common.kernel.View;
import geogebra.common.kernel.geos.GeoElement;
import geogebra.common.kernel.geos.GeoPoint;
import geogebra.common.main.settings.ConstructionProtocolSettings;
import geogebra.common.util.AsyncOperation;

import java.util.ArrayList;

/**
 * This interface is almost the same as GuiManager,
 * just it is an interface and doesn't implement anything,
 * and contains only public methods.
 * (So things from GuiManager were moved to here.)
 * 
 * @author arpad
 *
 */

public interface GuiManagerInterface {

	public void updateMenubar();

	public void updateMenubarSelection();

	public DialogManager getDialogManager();

	public void showPopupMenu(ArrayList<GeoElement> selectedGeos,
			EuclidianViewInterfaceCommon euclidianViewInterfaceCommon,
			GPoint mouseLoc);
	
	public void showPopupChooseGeo(ArrayList<GeoElement> selectedGeos,
			ArrayList<GeoElement> geos, EuclidianViewInterfaceCommon view,
			GPoint p);

	public void setMode(int mode,ModeSetter m);

	public void redo();
	public void undo();

	public void setFocusedPanel(AbstractEvent event, boolean updatePropertiesView);

	public void loadImage(GeoPoint loc, Object object, boolean altDown);

	public boolean hasAlgebraViewShowing();

	public boolean hasAlgebraView();
	
	public void updateFonts();

	public boolean isUsingConstructionProtocol();

	public void getConsProtocolXML(StringBuilder sb);

	public boolean isInputFieldSelectionListener();

	public geogebra.common.javax.swing.GTextComponent getAlgebraInputTextField();

	public void showDrawingPadPopup(EuclidianViewInterfaceCommon view,
			GPoint mouseLoc);
	
	public void showDrawingPadPopup3D(EuclidianViewInterfaceCommon view,
			GPoint mouseLoc);
	
	

	public boolean hasSpreadsheetView();

	public void attachSpreadsheetView();

	public void setShowView(boolean b, int viewID);
	
	public void setShowView(boolean b, int viewID, boolean isPermanent);

	public boolean showView(int viewID);

	public View getConstructionProtocolData();
	
	public View getCasView();
	
	public View getSpreadsheetView();
	
	public View getProbabilityCalculator();
	
	public View getDataAnalysisView();
	
	public View getPlotPanelView(int id);
	
	public View getPropertiesView();
	
	public View getAssignmentView();

	public boolean hasProbabilityCalculator();

	public void getProbabilityCalculatorXML(StringBuilder sb);

	public void getSpreadsheetViewXML(StringBuilder sb, boolean asPreference);

	public void getAlgebraViewXML(StringBuilder sb, boolean asPreference);

	public void updateActions();

	public void updateSpreadsheetColumnWidths();

	public void updateConstructionProtocol();

	public void updateAlgebraInput();

	public void setShowAuxiliaryObjects(boolean flag);

	public void updatePropertiesView();
	
	/**
	 * tells the properties view that mouse has been pressed
	 */
	public void mousePressedForPropertiesView();	
	
	/**
	 * tells the properties view that mouse has been released
	 * @param creatorMode tells if ev is in creator mode (ie not move mode)
	 */
	public void mouseReleasedForPropertiesView(boolean creatorMode);
	
	public boolean save();

	/**
	 * tells the properties view to show slider tab
	 */
	public void showPropertiesViewSliderTab();
	
	public void openURL();

	public boolean loadURL(String urlString);

	/**
	 * possible GeoGebraTube syntaxes
	 * http://www.geogebratube.org/material/show/id/111
	 * http://www.geogebratube.org/student/m111
	 * http://www.geogebratube.org/student/cXX/m111/options
	 * www.geogebratube.org/material/show/id/111
	 * www.geogebratube.org/student/m111
	 * www.geogebratube.org/student/cXX/m111/options
	 * http://geogebratube.org/material/show/id/111
	 * http://geogebratube.org/student/m111
	 * http://geogebratube.org/student/cXX/m111/options http://ggbtu.be/m111
	 * http://ggbtu.be/cXX/m111/options http://www.ggbtu.be/m111
	 * http://www.ggbtu.be/cXX/options
	 * 
	 * in an iframe, src=
	 * http://www.geogebratube.org/material/iframe/id/111
	 * http://www.geogebratube.org/material/iframe/id/111/param1/val1/param2/val2/...
	 * http://ggbtu.be/e111
	 * http://ggbtu.be/e111?param1=&param2=..
	 * 
	 * 
	 * also can have ?mobile=true ?mobile=false on end
	 */
	public boolean loadURL(String urlString, boolean suppressErrorMsg);

	public void updateGUIafterLoadFile(boolean success, boolean isMacroFile);

	public void startEditing(GeoElement geoElement);

	public boolean noMenusOpen();

	public void openFile();

	public Layout getLayout();

	public void showGraphicExport();

	public void showPSTricksExport();

	public void showWebpageExport();

	public void detachPropertiesView();

	public boolean hasPropertiesView();

	public void attachPropertiesView();

	public void attachAlgebraView();

	public void attachCasView();

	public void attachConstructionProtocolView();

	public void attachProbabilityCalculatorView();
	
	public void attachAssignmentView();
	
	public void attachDataAnalysisView();

	public void detachDataAnalysisView();
	
	public boolean hasDataAnalysisView();
	

	/**
	 * Attach a view which by using the view ID.
	 * 
	 * @author Florian Sonner
	 * @version 2008-10-21
	 * 
	 * @param viewId
	 */
	public void attachView(int viewId);
	
	public EuclidianView getActiveEuclidianView();

	public void showAxesCmd();

	public void showGridCmd();
	
	public void doAfterRedefine(GeoElement geo);

	/**
	 * Detach a view which by using the view ID.
	 * 
	 * @author Florian Sonner
	 * @version 2008-10-21
	 * 
	 * @param viewId
	 */
	public void detachView(int viewId);

	public void detachAssignmentView();

	public void detachProbabilityCalculatorView();

	public void detachCasView();

	public void detachConstructionProtocolView();

	public void detachSpreadsheetView();

	public void detachAlgebraView();

	public void openCommandHelp(String command);

	public void openHelp(String page);

	public void setLayout(Layout layout);

	public void initialize();

	public void resetSpreadsheet();

	public void setScrollToShow(boolean b);

	public void showURLinBrowser(String strURL);

	public void updateMenuWindow();

	public void updateMenuFile();

	public void exitAll();

	public boolean saveCurrentFile();

	public void updateToolbar();

	public boolean hasEuclidianView2();

	public void allowGUIToRefresh();

	public void updateFrameTitle();

	public void setLabels();

	public void setShowToolBarHelp(boolean showToolBarHelp);

	public View getEuclidianView2();

	public boolean hasEuclidianView2EitherShowingOrNot();

	public View getAlgebraView();
	
	public void applyAlgebraViewSettings();

	public void updateFrameSize();

	public void clearInputbar();

	public Object createFrame();

	public Object getInputHelpPanel();

	public int getInputHelpPanelMinimumWidth();
	/**
	 * 
	 * @return id of view which is setting the active toolbar
	 */
	public int getActiveToolbarId();

	public ConstructionProtocolView getConstructionProtocolView();

	public void setShowConstructionProtocolNavigation(boolean show);

	public void setShowConstructionProtocolNavigation(boolean show,
			boolean playButton, double playDelay, boolean showProtButton);

	public void updateCheckBoxesForShowConstructinProtocolNavigation();
	
	/*
	 * In web there are some drawable which not drawn on the canvas of euclidian view, but added for an AbsolutePanel which hides the canvas.
	 * (e.g. inputbox)
	 * When we remove all drawable, we must to clear this AbsolutePanel too.
	 */
	public void clearAbsolutePanels();

	/**
	 * #3490  "Create sliders for a, b?" Create Sliders / Cancel
 	 * Yes: create sliders and draw line
 	 * No: go back into input bar and allow user to change input
	 * @param string eg "a, b"
	 * @return true/false
	 */
	public boolean checkAutoCreateSliders(String string, AsyncOperation callback);

	public void applyCPsettings(ConstructionProtocolSettings cpSettings);

	public ConstructionProtocolNavigation getConstructionProtocolNavigationIfExists();
	
	ConstructionProtocolNavigation getConstructionProtocolNavigation();
	
	public void login();
	public void logout();
}
