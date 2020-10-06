package org.geogebra.web.full.gui.dialog;

import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.io.layout.PerspectiveDecoder;
import org.geogebra.common.kernel.arithmetic.SymbolicMode;
import org.geogebra.web.full.gui.GuiManagerW;
import org.geogebra.web.full.gui.images.SvgPerspectiveResources;
import org.geogebra.web.full.main.AppWFull;
import org.geogebra.web.html5.gui.GPopupPanel;
import org.geogebra.web.html5.gui.view.button.StandardButton;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.util.LoadFilePresenter;
import org.geogebra.web.html5.util.Persistable;
import org.geogebra.web.resources.SVGResource;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * Calculator chooser for suite
 */
public class CalculatorSwitcherDialog extends GPopupPanel implements Persistable, ResizeHandler {

	/**
	 * constructor
	 * @param app see {@link AppW}
	 */
	public CalculatorSwitcherDialog(AppW app) {
		super(true, app.getPanel(), app);
		setGlassEnabled(true);
		addStyleName("calcChooser");
		buildGUI();
		Window.addResizeHandler(this);
	}

	private void buildGUI() {
		FlowPanel contentPanel = new FlowPanel();
		Label title = new Label(app.getLocalization().getMenu("ChooseCalculator"));
		title.addStyleName("title");
		contentPanel.add(title);

		SvgPerspectiveResources res = SvgPerspectiveResources.INSTANCE;
		StandardButton btnGraphing = buildCalcButton(res.menu_icon_algebra_transparent(),
				GeoGebraConstants.GRAPHING_APPCODE, "GraphingCalculator.short");
		contentPanel.add(btnGraphing);

		StandardButton btn3D = buildCalcButton(res.menu_icon_graphics3D_transparent(),
				GeoGebraConstants.G3D_APPCODE, "GeoGebra3DGrapher.short");
		contentPanel.add(btn3D);

		StandardButton btnGeometry = buildCalcButton(res.menu_icon_geometry_transparent(),
				GeoGebraConstants.GEOMETRY_APPCODE, "Geometry");
		contentPanel.add(btnGeometry);

		StandardButton btnCAS = buildCalcButton(res.cas_white_bg(),
				GeoGebraConstants.CAS_APPCODE, "CAS");
		contentPanel.add(btnCAS);

		add(contentPanel);
	}

	private StandardButton buildCalcButton(SVGResource icon, String subAppCode,
			String appNameKey) {
		StandardButton button =  new StandardButton(app, 72, icon,
				 app.getLocalization().getMenu(appNameKey));
		button.setStyleName("calcBtn");
		if (subAppCode.equals(app.getConfig().getSubAppCode())) {
			button.addStyleName("selected");
		}

		button.addFastClickHandler(source -> {
			hide();
			AppWFull appWFull = ((AppWFull) app);
			appWFull.resetActivity(subAppCode);
			appWFull.getKernel().setSymbolicMode(
					GeoGebraConstants.CAS_APPCODE.equals(subAppCode)
							? SymbolicMode.SYMBOLIC_AV
							: SymbolicMode.NONE);
			appWFull.getGgbApi().setPerspective(app.getConfig().getForcedPerspective());
		});
		return button;
	}

	@Override
	public void show() {
		super.show();
		super.center();
	}

	@Override
	public void onResize(ResizeEvent event) {
		if (isShowing()) {
			super.center();
		}
	}
}
