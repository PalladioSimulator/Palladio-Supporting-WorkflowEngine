/**
 * 
 */
package de.uka.ipd.sdq.workflow.jobs;

import org.apache.log4j.Logger;

/**
 * An abstract job providing basic infrastructure such as logging.
 *
 */
public abstract class AbstractJob implements IJob {

    /** The logger. */
    protected Logger logger = Logger.getLogger(AbstractJob.class);

}
