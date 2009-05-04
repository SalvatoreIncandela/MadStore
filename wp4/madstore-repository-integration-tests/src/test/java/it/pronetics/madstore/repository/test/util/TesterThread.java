/**
 * Copyright 2008 - 2009 Pro-Netics S.P.A.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.pronetics.madstore.repository.test.util;


/**
 * Thread extension for recording test failures and verifying them.
 */
public class TesterThread extends Thread {

    private final Runnable test;
    private Exception exceptionFailure;
    private AssertionError assertionFailure;

    /**
     * Package access: use {@link TesterThreadFactory#newThread(Runnable )} instead.
     */
    TesterThread(Runnable test) {
        this.test = test;
    }
    
    public final void run() {
        try {
            this.test.run();
        } catch (Exception ex) {
            this.exceptionFailure = ex;
        } catch (AssertionError err) {
            this.assertionFailure = err;
        }
    }
    
    public void verifyAndThrow() throws AssertionError, Exception {
        if (this.exceptionFailure != null) {
            throw exceptionFailure;
        }
        if (this.assertionFailure != null) {
            throw assertionFailure;
        }
    }
}
