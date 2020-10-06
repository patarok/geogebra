package org.geogebra.web.full.main.activity;

import org.geogebra.web.full.css.MebisResources;
import org.geogebra.web.full.css.ResourceIconProvider;

import com.google.gwt.storage.client.Storage;

public class MebisNotesActivity extends NotesActivity {

	@Override
	public ResourceIconProvider getResourceIconProvider() {
		return MebisResources.INSTANCE;
	}

	@Override
	public void markSearchOpen() {
		markOpen("search:");
	}

	private void markOpen(String id) {
		Storage storage = Storage.getSessionStorageIfSupported();
		storage.setItem("tafelOpen", id);
	}

	@Override
	public void markSaveOpen() {
		markOpen("save:");
	}
}
