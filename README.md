AntialiasAdjust
===============

*Note*: This module was put together to solve a problem that doesn't exist anymore.  Older versions of Oracle JRE did not properly support retina (high DPI) displays, but that has been rectified for quite some time now.  As such this small project is not being maintained but kept here as a reference for building small add-on modules to NetBeans.

This is a Netbeans 7 plugin that provides full control over rendering hints.  I wrote this because after using Netbeans for years on Ubuntu and switching to Mac OSX, everything looked horribly fuzzy.  I hope this plugin helps you out!

Installing
----------
Grab the jar file (https://github.com/badvision/AntialiasAdjust/raw/master/org-badvision-hint.jar) and import from the plugins manager.  After activated, you do not need to restart.  Go to the preferences > Advanced screen and look for the Antialias tab.  Adjust to your liking.  Adjustments are updated as you make them (so the apply/ok buttons only save settings)  Since the font hints are only used when redrawing you will need to mouse-over or click on UI elements in order to really see the effect of your changes.

Compiling
---------
Since this is a Netbeans add-on, this is also structured as a Netbeans project.  I picked OSGi as a personal preference, and I really like it as a module standard.
