/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.badvision.hint;

import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 *
 * @author brobert
 */
public abstract class Antialias extends Toolkit {

    public static final String DESKTOP_HINTS = "awt.font.desktophints";

    static Map<RenderingHints.Key, Object[]> options;

    static {
        options = new HashMap<>();
        addOptions(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_DEFAULT,
                RenderingHints.VALUE_ANTIALIAS_OFF,
                RenderingHints.VALUE_ANTIALIAS_ON);
        addOptions(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_DEFAULT,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY,
                RenderingHints.VALUE_COLOR_RENDER_SPEED);
        addOptions(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT,
                RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        addOptions(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_DEFAULT,
                RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.VALUE_RENDER_SPEED);
        addOptions(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT,
                RenderingHints.VALUE_TEXT_ANTIALIAS_GASP,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static void addOptions(RenderingHints.Key key, Object... values) {
        options.put(key, values);
    }

    public static Object[] getOptions(RenderingHints.Key key) {
        return options.get(key);
    }

    public static void setOption(RenderingHints.Key key, Object value) {
        Map<RenderingHints.Key, Object> hints = getCurrentHints();
        if (value == null) {
            hints.remove(key);
            NbPreferences.forModule(Antialias.class).remove(key.toString());
        } else {
            hints.put(key, value);
            if (key.equals(RenderingHints.KEY_TEXT_LCD_CONTRAST)) {
                NbPreferences.forModule(Antialias.class).putInt(key.toString(), (Integer) value);
            } else {
                NbPreferences.forModule(Antialias.class).put(key.toString(), value.toString());
            }
        }
        updateHints(hints);
    }

    public static Object getOption(RenderingHints.Key key) {
        if (key.equals(RenderingHints.KEY_TEXT_LCD_CONTRAST)) {
            return NbPreferences.forModule(Antialias.class).getInt(key.toString(), 140);
        } else {
            String stored = NbPreferences.forModule(Antialias.class).get(key.toString(), null);
            for (Object[] vals : options.values()) {
                for (Object val : vals) {
                    if (val.toString().equals(stored)) {
                        return val;
                    }
                }
            }
        }
        return null;
    }

    private static Map<RenderingHints.Key, Object> getCurrentHints() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Map<RenderingHints.Key, Object> map = (Map<RenderingHints.Key, Object>) (tk.getDesktopProperty(DESKTOP_HINTS));
        if (map == null) {
            map = new HashMap<>();
        }
        return map;
    }

    private static void updateHints(final Map<RenderingHints.Key, Object> map) {
        final Toolkit tk = Toolkit.getDefaultToolkit();
        final Method m = findMethod(tk.getClass(), "setDesktopProperty", String.class, Object.class);
        m.setAccessible(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    m.invoke(tk, DESKTOP_HINTS, map);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        });
    }

    private static Method findMethod(Class clazz, String methodName, Class... types) {
        try {
            Method m = clazz.getDeclaredMethod(methodName, types);
            return m;
        } catch (NoSuchMethodException | SecurityException t) {
            if (clazz.equals(Object.class)) {
                return null;
            } else {
                return findMethod(clazz.getSuperclass(), methodName, types);
            }
        }
    }

    static void restoreValues() {
        for (RenderingHints.Key key : options.keySet()) {
            setOption(key, getOption(key));
        }
        setOption(RenderingHints.KEY_TEXT_LCD_CONTRAST, getOption(RenderingHints.KEY_TEXT_LCD_CONTRAST));
    }
}
