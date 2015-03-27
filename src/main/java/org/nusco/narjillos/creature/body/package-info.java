/**
 * The physical body of a Narjillo.
 * 
 * Contains the {@link org.nusco.narjillos.creature.body.Body} class, that acts as a
 * Facade to multiple parts of the Narjillo's body---all of them objects of the
 * {@link org.nusco.narjillos.creature.body.Organ} hierarchy.
 * 
 * This is probably the most messed-up and experimental code in the whole system, and
 * the one that was hardest to write (and re-write; and re-write again). ;)
 * The classes in this package are also responsible for the most of the calculations
 * done in the system, so an optimization here can reap large performance benefits.
 */
package org.nusco.narjillos.creature.body;

