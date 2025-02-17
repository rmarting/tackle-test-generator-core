/*
Copyright IBM Corporation 2021

Licensed under the Eclipse Public License 2.0, Version 2.0 (the "License");
you may not use this file except in compliance with the License.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.konveyor.tackle.testgen.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TackleTestLogger {

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
            "[%1$tF %1$tT] [%4$-7s] [%2$s] %5$s %n");
    }

    private static Level DEFAULT_LOG_LEVEL = Level.OFF;

    public static Logger getLogger(Class<?> cls) {
        Logger logger = Logger.getLogger(cls.getSimpleName());
        Handler handler = new ConsoleHandler();
        logger.addHandler(handler);
        logger.setLevel(DEFAULT_LOG_LEVEL);
        logger.setUseParentHandlers(false);
        return logger;
    }

}
