package com.example.seamcarving;


import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {
    public T[] data;
    private double[] priorities;
    private Map<T, Integer> hashPriority;
    private int size;

    public ArrayHeapMinPQ() {
        data = makeArray(10);
        priorities = new double[10];
        size = 0;
        data[0] = null;
        hashPriority = new HashMap<>();
    }

    /*
    Here's a helper method and a method stub that may be useful. Feel free to change or remove
    them, if you wish.
     */

    /**
     * A helper method to create arrays of T, in case you're using an array to represent your heap.
     * You shouldn't need this if you're using an ArrayList instead.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArray(int newCapacity) {
        return (T[]) new Object[newCapacity];
    }

    /**
     * A helper method for swapping the items at two indices of the array heap.
     */
    private void swap(int a, int b) {
        //swap in hash
        hashPriority.put(data[a], b);
        hashPriority.put(data[b], a);


        T temp = data[a];
        data[a] = data[b];
        data[b] = temp;

        double temp2 = priorities[a];
        priorities[a] = priorities[b];
        priorities[b] = temp2;

    }


    /**
     * Adds an item with the given priority value.
     * Assumes that item is never null.
     * Runs in O(log N) time (except when resizing).
     * @throws IllegalArgumentException if item is already present in the PQ
     */
    @Override
    public void add(T item, double priority) {
        if (contains(item)){
            throw new IllegalArgumentException();
        }
        if (size == data.length - 1){
           T[] newArr = makeArray((int) (1.333*data.length));
           double[] newpriority = new double[(int) (1.333*data.length)];
           for (int i = 0; i < data.length; i++){
              newArr[i] = data[i];
              newpriority[i] = priorities[i];
           }
           data = newArr;
           priorities = newpriority;
        }
        hashPriority.put(item, size+1);
        data[size + 1] = item;
        priorities[size + 1] = priority;
        swim(size + 1);
        size++;
    }

    private void swim(int currIndex){
        int parentIndex = (currIndex)/2;
        //priority of current node is lower than parent, swap
        if (parentIndex >= 1 && priorities[currIndex] < priorities[parentIndex]){
            swap(currIndex, parentIndex);
            swim(parentIndex);
        }
    }

    private void sink(int currIndex){
        int childIndexR = 2*currIndex + 1;
        int childIndexL = 2*currIndex;
        int smallerIndex = 0;
        if (childIndexL < data.length - 1 && childIndexR < data.length - 1 && data[childIndexL] != null) {
            if (data[childIndexR] != null){
                smallerIndex = priorities[childIndexL] < priorities[childIndexR] ? childIndexL : childIndexR;
                if (priorities[currIndex] > priorities[smallerIndex]){
                    swap(currIndex, smallerIndex);
                    sink(smallerIndex);
                }
            } else if (priorities[currIndex] > priorities[childIndexL]) {
                swap(currIndex, childIndexL);
                sink(childIndexL);
            }
        }
    }





    /**
     * Returns true if the PQ contains the given item; false otherwise.
     * Runs in O(log N) time.
     */
    @Override
    public boolean contains(T item) {
        return hashPriority.containsKey(item);
    }

    /**
     * Returns the item with the smallest priority.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T getSmallest() {
        if (size == 0){
            throw new NoSuchElementException();
        }
        return data[1];
    }

    /**
     * Removes and returns the item with the smallest priority.
     * Runs in O(log N) time (except when resizing).
     * @throws NoSuchElementException if the PQ is empty
     */
    @Override
    public T removeSmallest() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        hashPriority.remove(data[1]);
        T temp = data[1];
        data[1] = data[size];
        data[size] = null;

        double temp2 = priorities[1];
        priorities[1] = priorities[size];
        priorities[size] = 0;

        size--;
        hashPriority.put(data[1], 1);
        sink(1);
        return temp;
    }

    /**
     * Changes the priority of the given item.
     * Runs in O(log N) time.
     * @throws NoSuchElementException if the item is not present in the PQ
     */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)){
            throw new NoSuchElementException();
        }
        int index = hashPriority.get(item);
        priorities[index] = priority;

        sink(index);
        swim(index);
    }

    /**
     * Returns the number of items in the PQ.
     * Runs in O(log N) time.
     */
    @Override
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

}
