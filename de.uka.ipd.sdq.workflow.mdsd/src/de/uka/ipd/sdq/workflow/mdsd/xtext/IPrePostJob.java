package de.uka.ipd.sdq.workflow.mdsd.xtext;

import de.uka.ipd.sdq.workflow.jobs.IJob;

/**
 * A job supporting pre and post execute routines.
 *
 * @author Joerg Henss
 */
public interface IPrePostJob extends IJob {

    /**
     * Executed prior execute().
     */
    public void preExecute();

    /**
     * Executed after execute().
     */
    public void postExecute();

}
