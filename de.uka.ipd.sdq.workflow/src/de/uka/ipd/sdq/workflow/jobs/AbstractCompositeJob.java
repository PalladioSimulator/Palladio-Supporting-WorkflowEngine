package de.uka.ipd.sdq.workflow.jobs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * The Class AbstractCompositeJob.
 */
public abstract class AbstractCompositeJob implements ICompositeJob, List<IJob> {

    /** The my jobs. */
    protected LinkedList<IJob> myJobs;

    /** The my executed jobs. */
    protected Stack<IJob> myExecutedJobs;

    /** The logger. */
    protected Logger logger = Logger.getLogger(SequentialJob.class);

    /** The my name. */
    private String myName = null;

    /**
     * Instantiates a new abstract composite job.
     */
    public AbstractCompositeJob() {
        super();
        myJobs = new LinkedList<IJob>();
        myExecutedJobs = new Stack<IJob>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.ICompositeJob#addJob(de.uka.ipd.sdq.workflow.IJob)
     */
    @Override
    public void addJob(IJob job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }

        myJobs.add(job);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#execute(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public abstract void execute(IProgressMonitor monitor) throws JobFailedException, UserCanceledException;

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ipd.sdq.workflow.IJob#getName()
     */
    @Override
    public String getName() {
        if (myName != null) {
            return myName;
        }

        String compositeName = "CompositeJob <";
        for (IJob job : myJobs) {
            compositeName += job.getName() + " ";
        }
        compositeName += ">";

        return compositeName;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.myName = name;
    }

    /**
     * {@inheritDoc}<br>
     * <br>
     * This method ensures to clean up all child jobs in the reverse order in which they were added.
     * 
     */
    public void cleanup(IProgressMonitor monitor) throws CleanupFailedException {
        monitor.subTask("Cleanup of " + getName());
        while (!myExecutedJobs.empty()) {
            myExecutedJobs.pop().cleanup(monitor);
            monitor.worked(1);
        }
    }

    /**
     * Adds the.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see java.util.LinkedList#add(java.lang.Object)
     */
    public boolean add(IJob e) {
        return myJobs.add(e);
    }

    /**
     * Adds the.
     * 
     * @param index
     *            the index
     * @param element
     *            the element
     * @see java.util.LinkedList#add(int, java.lang.Object)
     */
    public void add(int index, IJob element) {
        myJobs.add(index, element);
    }

    /**
     * Adds the all.
     * 
     * @param c
     *            the c
     * @return true, if successful
     * @see java.util.LinkedList#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends IJob> c) {
        return myJobs.addAll(c);
    }

    /**
     * Adds the all.
     * 
     * @param index
     *            the index
     * @param c
     *            the c
     * @return true, if successful
     * @see java.util.LinkedList#addAll(int, java.util.Collection)
     */
    public boolean addAll(int index, Collection<? extends IJob> c) {
        return myJobs.addAll(index, c);
    }

    /**
     * Adds the first.
     * 
     * @param e
     *            the e
     * @see java.util.LinkedList#addFirst(java.lang.Object)
     */
    public void addFirst(IJob e) {
        myJobs.addFirst(e);
    }

    /**
     * Adds the last.
     * 
     * @param e
     *            the e
     * @see java.util.LinkedList#addLast(java.lang.Object)
     */
    public void addLast(IJob e) {
        myJobs.addLast(e);
    }

    /**
     * Clear.
     * 
     * @see java.util.LinkedList#clear()
     */
    public void clear() {
        myJobs.clear();
    }

    /**
     * Clone.
     * 
     * @return the object
     * @see java.util.LinkedList#clone()
     */
    public Object clone() {
        return myJobs.clone();
    }

    /**
     * Contains.
     * 
     * @param o
     *            the o
     * @return true, if successful
     * @see java.util.LinkedList#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return myJobs.contains(o);
    }

    /**
     * Contains all.
     * 
     * @param c
     *            the c
     * @return true, if successful
     * @see java.util.AbstractCollection#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> c) {
        return myJobs.containsAll(c);
    }

    /**
     * Descending iterator.
     * 
     * @return the iterator
     * @see java.util.LinkedList#descendingIterator()
     */
    public Iterator<IJob> descendingIterator() {
        return myJobs.descendingIterator();
    }

    /**
     * Element.
     * 
     * @return the i job
     * @see java.util.LinkedList#element()
     */
    public IJob element() {
        return myJobs.element();
    }

    /**
     * Equals.
     * 
     * @param o
     *            the o
     * @return true, if successful
     * @see java.util.AbstractList#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        return myJobs.equals(o);
    }

    /**
     * Gets the.
     * 
     * @param index
     *            the index
     * @return the i job
     * @see java.util.LinkedList#get(int)
     */
    public IJob get(int index) {
        return myJobs.get(index);
    }

    /**
     * Gets the first.
     * 
     * @return the first
     * @see java.util.LinkedList#getFirst()
     */
    public IJob getFirst() {
        return myJobs.getFirst();
    }

    /**
     * Gets the last.
     * 
     * @return the last
     * @see java.util.LinkedList#getLast()
     */
    public IJob getLast() {
        return myJobs.getLast();
    }

    /**
     * Hash code.
     * 
     * @return the int
     * @see java.util.AbstractList#hashCode()
     */
    public int hashCode() {
        return myJobs.hashCode();
    }

    /**
     * Index of.
     * 
     * @param o
     *            the o
     * @return the int
     * @see java.util.LinkedList#indexOf(java.lang.Object)
     */
    public int indexOf(Object o) {
        return myJobs.indexOf(o);
    }

    /**
     * Checks if is empty.
     * 
     * @return true, if is empty
     * @see java.util.AbstractCollection#isEmpty()
     */
    public boolean isEmpty() {
        return myJobs.isEmpty();
    }

    /**
     * Iterator.
     * 
     * @return the iterator
     * @see java.util.AbstractSequentialList#iterator()
     */
    public Iterator<IJob> iterator() {
        return myJobs.iterator();
    }

    /**
     * Last index of.
     * 
     * @param o
     *            the o
     * @return the int
     * @see java.util.LinkedList#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object o) {
        return myJobs.lastIndexOf(o);
    }

    /**
     * List iterator.
     * 
     * @return the list iterator
     * @see java.util.AbstractList#listIterator()
     */
    public ListIterator<IJob> listIterator() {
        return myJobs.listIterator();
    }

    /**
     * List iterator.
     * 
     * @param index
     *            the index
     * @return the list iterator
     * @see java.util.LinkedList#listIterator(int)
     */
    public ListIterator<IJob> listIterator(int index) {
        return myJobs.listIterator(index);
    }

    /**
     * Offer.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see java.util.LinkedList#offer(java.lang.Object)
     */
    public boolean offer(IJob e) {
        return myJobs.offer(e);
    }

    /**
     * Offer first.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see java.util.LinkedList#offerFirst(java.lang.Object)
     */
    public boolean offerFirst(IJob e) {
        return myJobs.offerFirst(e);
    }

    /**
     * Offer last.
     * 
     * @param e
     *            the e
     * @return true, if successful
     * @see java.util.LinkedList#offerLast(java.lang.Object)
     */
    public boolean offerLast(IJob e) {
        return myJobs.offerLast(e);
    }

    /**
     * Peek.
     * 
     * @return the i job
     * @see java.util.LinkedList#peek()
     */
    public IJob peek() {
        return myJobs.peek();
    }

    /**
     * Peek first.
     * 
     * @return the i job
     * @see java.util.LinkedList#peekFirst()
     */
    public IJob peekFirst() {
        return myJobs.peekFirst();
    }

    /**
     * Peek last.
     * 
     * @return the i job
     * @see java.util.LinkedList#peekLast()
     */
    public IJob peekLast() {
        return myJobs.peekLast();
    }

    /**
     * Poll.
     * 
     * @return the i job
     * @see java.util.LinkedList#poll()
     */
    public IJob poll() {
        return myJobs.poll();
    }

    /**
     * Poll first.
     * 
     * @return the i job
     * @see java.util.LinkedList#pollFirst()
     */
    public IJob pollFirst() {
        return myJobs.pollFirst();
    }

    /**
     * Poll last.
     * 
     * @return the i job
     * @see java.util.LinkedList#pollLast()
     */
    public IJob pollLast() {
        return myJobs.pollLast();
    }

    /**
     * Pop.
     * 
     * @return the i job
     * @see java.util.LinkedList#pop()
     */
    public IJob pop() {
        return myJobs.pop();
    }

    /**
     * Push.
     * 
     * @param e
     *            the e
     * @see java.util.LinkedList#push(java.lang.Object)
     */
    public void push(IJob e) {
        myJobs.push(e);
    }

    /**
     * Removes the.
     * 
     * @return the i job
     * @see java.util.LinkedList#remove()
     */
    public IJob remove() {
        return myJobs.remove();
    }

    /**
     * Removes the.
     * 
     * @param index
     *            the index
     * @return the i job
     * @see java.util.LinkedList#remove(int)
     */
    public IJob remove(int index) {
        return myJobs.remove(index);
    }

    /**
     * Removes the.
     * 
     * @param o
     *            the o
     * @return true, if successful
     * @see java.util.LinkedList#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return myJobs.remove(o);
    }

    /**
     * Removes the all.
     * 
     * @param c
     *            the c
     * @return true, if successful
     * @see java.util.AbstractCollection#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        return myJobs.removeAll(c);
    }

    /**
     * Removes the first.
     * 
     * @return the i job
     * @see java.util.LinkedList#removeFirst()
     */
    public IJob removeFirst() {
        return myJobs.removeFirst();
    }

    /**
     * Removes the first occurrence.
     * 
     * @param o
     *            the o
     * @return true, if successful
     * @see java.util.LinkedList#removeFirstOccurrence(java.lang.Object)
     */
    public boolean removeFirstOccurrence(Object o) {
        return myJobs.removeFirstOccurrence(o);
    }

    /**
     * Removes the last.
     * 
     * @return the i job
     * @see java.util.LinkedList#removeLast()
     */
    public IJob removeLast() {
        return myJobs.removeLast();
    }

    /**
     * Removes the last occurrence.
     * 
     * @param o
     *            the o
     * @return true, if successful
     * @see java.util.LinkedList#removeLastOccurrence(java.lang.Object)
     */
    public boolean removeLastOccurrence(Object o) {
        return myJobs.removeLastOccurrence(o);
    }

    /**
     * Retain all.
     * 
     * @param c
     *            the c
     * @return true, if successful
     * @see java.util.AbstractCollection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        return myJobs.retainAll(c);
    }

    /**
     * Sets the.
     * 
     * @param index
     *            the index
     * @param element
     *            the element
     * @return the i job
     * @see java.util.LinkedList#set(int, java.lang.Object)
     */
    public IJob set(int index, IJob element) {
        return myJobs.set(index, element);
    }

    /**
     * Size.
     * 
     * @return the int
     * @see java.util.LinkedList#size()
     */
    public int size() {
        return myJobs.size();
    }

    /**
     * Sub list.
     * 
     * @param fromIndex
     *            the from index
     * @param toIndex
     *            the to index
     * @return the list
     * @see java.util.AbstractList#subList(int, int)
     */
    public List<IJob> subList(int fromIndex, int toIndex) {
        return myJobs.subList(fromIndex, toIndex);
    }

    /**
     * To array.
     * 
     * @return the object[]
     * @see java.util.LinkedList#toArray()
     */
    public Object[] toArray() {
        return myJobs.toArray();
    }

    /**
     * To array.
     * 
     * @param <T>
     *            the generic type
     * @param a
     *            the a
     * @return the t[]
     * @see java.util.LinkedList#toArray(T[])
     */
    public <T> T[] toArray(T[] a) {
        return myJobs.toArray(a);
    }

    /**
     * To string.
     * 
     * @return the string
     * @see java.util.AbstractCollection#toString()
     */
    public String toString() {
        return myJobs.toString();
    }

}
