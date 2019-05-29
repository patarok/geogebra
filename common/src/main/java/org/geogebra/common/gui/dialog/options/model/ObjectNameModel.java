package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.gui.dialog.handler.RedefineInputHandler;
import org.geogebra.common.gui.dialog.handler.RenameInputHandler;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.arithmetic.TextValue;
import org.geogebra.common.kernel.geos.GProperty;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoSymbolic;
import org.geogebra.common.kernel.geos.GeoText;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.App;
import org.geogebra.common.main.Feature;
import org.geogebra.common.main.error.ErrorHandler;
import org.geogebra.common.scientific.LabelController;
import org.geogebra.common.util.AsyncOperation;

public class ObjectNameModel extends OptionsModel {
	private IObjectNameListener listener;
	private RenameInputHandler nameInputHandler;
	private RedefineInputHandler defInputHandler;
	private GeoElementND currentGeo;
	private boolean redefinitionFailed;
	private boolean busy;
	private LabelController labelController=null;

	public interface IObjectNameListener extends PropertyListener {
		void setNameText(final String text);

		void setDefinitionText(final String text);

		void setCaptionText(final String text);

		void updateGUI(boolean showDefinition, boolean showCaption);

		void updateDefLabel();

		void updateCaption(final String text);

		void updateName(final String text);
	}

	public ObjectNameModel(App app, IObjectNameListener listener) {
		super(app);
		this.listener = listener;
		busy = false;
		redefinitionFailed = false;
		setNameInputHandler(new RenameInputHandler(app, null, false));
		// DEFINITON PANEL
		setDefInputHandler(new RedefineInputHandler(app, null, null));

	}

	@Override
	public void updateProperties() {
		/*
		 * DON'T WORK : MAKE IT A TRY FOR 5.0 ? //apply textfields modification
		 * on previous geo before switching to new geo //skip this if label is
		 * not set (we re in the middle of redefinition) //skip this if action
		 * is performing if (currentGeo!=null && currentGeo.isLabelSet() &&
		 * !actionPerforming && (geos.length!=1 || geos[0]!=currentGeo)){
		 * 
		 * //App.printStacktrace("\n"+tfName.getText()+"\n"+currentGeo.getLabel(
		 * StringTemplate.defaultTemplate));
		 * 
		 * String strName = tfName.getText(); if (strName !=
		 * currentGeo.getLabel(StringTemplate.defaultTemplate))
		 * nameInputHandler.processInput(tfName.getText());
		 * 
		 * 
		 * String strDefinition = tfDefinition.getText(); if
		 * (strDefinition.length()>0 &&
		 * !strDefinition.equals(getDefText(currentGeo)))
		 * defInputHandler.processInput(strDefinition);
		 * 
		 * String strCaption = tfCaption.getText(); if
		 * (!strCaption.equals(currentGeo.getCaptionSimple())){
		 * currentGeo.setCaption(tfCaption.getText());
		 * currentGeo.updateVisualStyleRepaint(); } }
		 */

		// take name of first geo
		GeoElement geo0 = getGeoAt(0);
		updateName(geo0);

		// if a focus lost is called in between, we keep the current definition
		// text
		// redefinitionForFocusLost = tfDefinition.getText();
		setCurrentGeo(geo0);
		nameInputHandler.setGeoElement(geo0);
		defInputHandler.setGeoElement(geo0);

		// DEFINITION
		// boolean showDefinition = !(currentGeo.isGeoText() ||
		// currentGeo.isGeoImage());
		boolean showDefinition = true;
		if(getCurrentGeo().isGeoText()){
			showDefinition = ((GeoText) getCurrentGeo()).isTextCommand();
		}else{
			showDefinition = getCurrentGeo().isAlgebraViewEditable();
		}

		if (showDefinition) {
			listener.updateDefLabel();
		}
		// CAPTION
		boolean showCaption = !(getCurrentGeo() instanceof TextValue); // borcherds
																		// was
		// currentGeo.isGeoBoolean();
		if (showCaption) {
			listener.updateCaption(getCurrentGeo().getRawCaption());
		}
		// captionLabel.setVisible(showCaption);
		// inputPanelCap.setVisible(showCaption);

		listener.updateGUI(showDefinition, showCaption);

	}

	private void updateName(GeoElement geo) {
		String name = "";
		if (!isAutoLabelNeeded(geo) || getLabelController().hasLabel(geo)) {
			name = geo.getLabel(StringTemplate.editTemplate);
		}
		listener.updateName(name);
	}

	@Override
	public boolean checkGeos() {
		return (getGeosLength() == 1);
	}

	public void applyNameChange(final String name, ErrorHandler handler) {
		if (isAutoLabelNeeded(getCurrentGeo())) {
			showLabel(currentGeo);
			boolean autoLabelJustAdded = "".equals(name);
			if (autoLabelJustAdded) {
				return;
			}
		}

		nameInputHandler.setGeoElement(currentGeo);
		nameInputHandler.processInput(name, handler,
				new AsyncOperation<Boolean>() {

					@Override
					public void callback(Boolean obj) {
						// TODO Auto-generated method stub

					}
				});

		// reset label if not successful
		final String strName = currentGeo
				.getLabel(StringTemplate.defaultTemplate);
		if (!strName.equals(name)) {
			listener.setNameText(strName);
		}
		currentGeo.updateRepaint();
		storeUndoInfo();

	}


	/**
	 *
	 * @param
	 * 			geo the geo eelement
	 * @return
	 * 			true if auto-labeling in CAS is needed.
	 */
	protected boolean isAutoLabelNeeded(GeoElementND geo) {
		if (!app.has(Feature.AUTOLABEL_CAS_SETTINGS)) {
			return false;
		}
		return geo instanceof GeoSymbolic;
	}

	private void ensureLabelOfCurrentGeo() {
		if (!hasLabel(currentGeo)) {
			showLabel(currentGeo);
		}
	}
	private void showLabel(GeoElementND geo) {
		getLabelController().showLabel((GeoElement) geo);
	}

	private boolean hasLabel(GeoElementND geo) {
		return getLabelController().hasLabel((GeoElement) geo);
	}

	private LabelController getLabelController() {
		if (labelController == null) {
			labelController = new LabelController();
		}
		return labelController;
	}

	public void applyDefinitionChange(final String definition,
			ErrorHandler handler) {
		if (!definition.equals(getDefText(currentGeo))) {
			defInputHandler.processInput(definition, handler,
					new AsyncOperation<Boolean>() {

						@Override
						public void callback(Boolean ok) {
							if (ok) {
								// if succeeded, switch current geo
								currentGeo = defInputHandler.getGeoElement();
								app.getSelectionManager()
										.clearSelectedGeos(false, false);
								app.getSelectionManager()
										.addSelectedGeo(currentGeo);
							} else {
								setRedefinitionFailed(true);
							}
							storeUndoInfo();
						}
					});

		}

	}

	public static String getDefText(GeoElementND geo) {
		/*
		 * return geo.isIndependent() ? geo.toOutputValueString() :
		 * geo.getCommandDescription();
		 */
		return geo.getRedefineString(false, true);
	}

	public void applyCaptionChange(final String caption) {
		currentGeo.setCaption(caption);

		final String strCaption = currentGeo.getRawCaption();
		if (!strCaption.equals(caption.trim())) {
			listener.setCaptionText(strCaption);
		}
		currentGeo.updateVisualStyleRepaint(GProperty.CAPTION);
		storeUndoInfo();
	}

	public void redefineCurrentGeo(GeoElementND geo, final String text,
			final String redefinitionText, ErrorHandler handler) {
		setBusy(true);

		if (isRedefinitionFailed()) {
			setRedefinitionFailed(false);
			return;
		}

		if (currentGeo == geo) {
			if (!text.equals(getDefText(currentGeo))) {

				listener.setDefinitionText(text);
				defInputHandler.setGeoElement(geo);
				defInputHandler.processInput(text, handler,
						new AsyncOperation<Boolean>() {

							@Override
							public void callback(Boolean ok) {
								if (ok) {
									setCurrentGeo(
											defInputHandler.getGeoElement());
									storeUndoInfo();
								}

							}
						});

			}
		} else {
			String strDefinition = redefinitionText;
			if (!strDefinition.equals(getDefText(geo))) {
				defInputHandler.setGeoElement(geo);
				defInputHandler.processInput(strDefinition, handler,
						new AsyncOperation<Boolean>() {

							@Override
							public void callback(Boolean obj) {
								// TODO Auto-generated method stub

							}
						});
				defInputHandler.setGeoElement(currentGeo);
			}
		}

	}

	public GeoElementND getCurrentGeo() {
		return currentGeo;
	}

	public void setCurrentGeo(GeoElementND currentGeo) {
		this.currentGeo = currentGeo;
	}

	public RenameInputHandler getNameInputHandler() {
		return nameInputHandler;
	}

	public void setNameInputHandler(RenameInputHandler nameInputHandler) {
		this.nameInputHandler = nameInputHandler;
	}

	public RedefineInputHandler getDefInputHandler() {
		return defInputHandler;
	}

	public void setDefInputHandler(RedefineInputHandler defInputHandler) {
		this.defInputHandler = defInputHandler;
	}

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	protected boolean isRedefinitionFailed() {
		return redefinitionFailed;
	}

	protected void setRedefinitionFailed(boolean redefinitionFailed) {
		this.redefinitionFailed = redefinitionFailed;
	}

	@Override
	protected boolean isValidAt(int index) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public PropertyListener getListener() {
		return listener;
	}

}
