/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.badvision.hint;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Manages a bundle's lifecycle. Remember that an activator is optional and
 * often not needed at all.
 */
public class Installer implements BundleActivator {

    @Override
    public void start(BundleContext c) throws Exception {
        Antialias.restoreValues();
    }

    @Override
    public void stop(BundleContext c) throws Exception {
    }
}
