/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.geogebra.web.web.gui.menubar;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.Widget;

/**Accessible alternative to MenuBar*/
public class AriaMenuBar extends Widget {
	private MenuItem selectedItem;
	private HashMap<MenuItem, Element> domItems = new HashMap<MenuItem, Element>();
	private ArrayList<MenuItem> allItems = new ArrayList<MenuItem>();

	public AriaMenuBar() {
		setElement(Document.get().createULElement());
		sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT
				| Event.ONFOCUS | Event.ONKEYDOWN);
	}

	public MenuItem addItem(MenuItem a) {
		a.getScheduledCommand();
		Element li = DOM.createElement("LI");
		li.setInnerHTML(a.getElement().getInnerHTML());
		li.setClassName("gwt-MenuItem listMenuItem");
		li.setAttribute("role", "menuitem");
		getElement().appendChild(li);
		allItems.add(a);
		domItems.put(a, li);
		return a;
	}

	/**
	 * Adds a menu item to the bar, that will fire the given command when it is
	 * selected.
	 *
	 * @param text
	 *            the item's text
	 * @param asHTML
	 *            <code>true</code> to treat the specified text as html
	 * @param cmd
	 *            the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, boolean asHTML, ScheduledCommand cmd) {
		return addItem(new MenuItem(text, asHTML, cmd));
	}

	public void focus() {
		// TODO Auto-generated method stub

	}

	protected MenuItem getSelectedItem() {
		return this.selectedItem;
	}

	/**
	 * Get the index of a {@link MenuItem}.
	 *
	 * @return the index of the item, or -1 if it is not contained by this
	 *         MenuBar
	 */
	public int getItemIndex(MenuItem item) {
		return allItems.indexOf(item);
	}

	public void selectItem(MenuItem item) {
		if (selectedItem != null) {
			domItems.get(selectedItem).addClassName("subMenuIcon-selected");
		}
		this.selectedItem = item;
		if (item != null) {
			domItems.get(item).addClassName("subMenuIcon-selected");
		}
	}

	public void clearItems() {
		allItems.clear();
		domItems.clear();
		selectItem(null);
	}

	public ArrayList<MenuItem> getItems() {
		return allItems;
	}

	public void moveSelectionDown() {
		int next = allItems.indexOf(selectedItem) + 1;
		if (next < allItems.size()) {
			selectItem(allItems.get(next));
		}
	}

	public void moveSelectionUp() {
		int next = allItems.indexOf(selectedItem) - 1;
		if (next >= 0 && next < allItems.size()) {
			selectItem(allItems.get(next));
		}
	}

	public MenuItemSeparator addSeparator() {
		return new MenuItemSeparator();
	}

	public void addSeparator(MenuItemSeparator sep) {
	}

	public void setAutoOpen(boolean b) {
		// TODO Auto-generated method stub

	}

	public MenuItem addItem(String itemtext, boolean textishtml,
			MenuBar submenupopup) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onBrowserEvent(Event event) {
		MenuItem item = findItem(DOM.eventGetTarget(event));
		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK: {
			// TODOFocusPanel.impl.focus(getElement());
			// Fire an item's command when the user clicks on it.
			if (item != null) {
				doItemAction(item, true, true);
			}
			break;
		}

		case Event.ONMOUSEOVER: {
			if (item != null) {
				itemOver(item, true);
			}
			break;
		}

		case Event.ONMOUSEOUT: {
			if (item != null) {
				itemOver(null, false);
			}
			break;
		}

		case Event.ONFOCUS: {
			// selectFirstItemIfNoneSelected();
			break;
		}

		case Event.ONKEYDOWN: {
			// int keyCode = event.getKeyCode();
			// boolean isRtl = LocaleInfo.getCurrentLocale().isRTL();
			// keyCode = KeyCodes.maybeSwapArrowKeysForRtl(keyCode, isRtl);
			// switch (keyCode) {
			// case KeyCodes.KEY_LEFT:
			// moveToPrevItem();
			// eatEvent(event);
			// break;
			// case KeyCodes.KEY_RIGHT:
			// moveToNextItem();
			// eatEvent(event);
			// break;
			// case KeyCodes.KEY_UP:
			// moveSelectionUp();
			// eatEvent(event);
			// break;
			// case KeyCodes.KEY_DOWN:
			// moveSelectionDown();
			// eatEvent(event);
			// break;
			// case KeyCodes.KEY_ESCAPE:
			// closeAllParentsAndChildren();
			// eatEvent(event);
			// break;
			// case KeyCodes.KEY_TAB:
			// closeAllParentsAndChildren();
			// break;
			// case KeyCodes.KEY_ENTER:
			// if (!selectFirstItemIfNoneSelected()) {
			// doItemAction(selectedItem, true, true);
			// eatEvent(event);
			// }
			// break;
			// } // end switch(keyCode)

			break;
		} // end case Event.ONKEYDOWN
		} // end switch (DOM.eventGetType(event))
		super.onBrowserEvent(event);
	}

	private MenuItem findItem(
			Element eventTarget) {
		for(MenuItem it: allItems){
			if (domItems.get(it).isOrHasChild(eventTarget)) {
				return it;
			}
		}
		return null;
	}
	private void doItemAction(MenuItem item, boolean b, boolean c) {
		final ScheduledCommand cmd = item.getScheduledCommand();
		Scheduler.get().scheduleFinally(new Scheduler.ScheduledCommand() {
			@Override
			public void execute() {
				cmd.execute();
			}
		});

	}
	private void itemOver(MenuItem item, boolean focus) {
		selectItem(item);
		// if (item != null) {
		// if ((shownChildMenu != null) || (parentMenu != null) || autoOpen) {
		// doItemAction(item, false, focusOnHover);
		// }
		// }
	}
}
