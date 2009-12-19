package org.seage.metaheuristic.tabusearch;


/**
 * This version of the {@link TabuSearch} does not create any new threads, making it
 * ideal for embedding in Enterprise JavaBeans. The {@link #startSolving} method
 * blocks until the given number of iterations have been completed.
 *
 *
 *
 *<p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
 * The Common Public License, developed by IBM and modeled after their industry-friendly IBM Public License,
 * differs from other common open source licenses in several important ways:
 * <ul>
 *  <li>You may include this software with other software that uses a different (even non-open source) license.</li>
 *  <li>You may use this software to make for-profit software.</li>
 *  <li>Your patent rights, should you generate patents, are protected.</li>
 * </ul>
 * </p>
 *
 *
 * @author  Robert Harder
 * @author  Richard Malek
 * @copyright 2000 Robert Harder
 * @version 1.0c
 * @since 1.0
 */
public class TabuSearch extends TabuSearchBase
{
    
    
    /** Objective function. */
    protected ObjectiveFunction objectiveFunction;
    
    /** Move manager. */
    protected MoveManager moveManager;
    
    /** Tabu list. */
    protected TabuList tabuList;
    
    /** Aspiration criteria. */
    protected AspirationCriteria aspirationCriteria;

	/** Long term memory	 */
	protected LongTermMemory longTermMemory;
    
    /** Current solution. */
    protected Solution currentSolution;
    
    /** Best solution. */
    protected Solution bestSolution;

	//protected SolutionComparator solutionComparator;
    
    /** Iterations to go. */
    protected int iterationsToGo;

    protected int numberOfIterations;

    //protected long startTime;
    protected long timeout = Long.MAX_VALUE;
    
    /** Maximizing: true. Minimizing: false. */
    protected boolean maximizing;
    
    /** Whether or not the the tabu search is solving. */
    protected boolean solving;
    
    /** Whether or not the tabu search should keep solving if it gets a chance to quit. */
    protected boolean keepSolving;
    
    /** Fire new current solution event at the end of the iteration. */
    protected boolean fireNewCurrentSolution;
    
    /** Fire new best solution event at the end of the iteration. */
    protected boolean fireNewBestSolution;
    
    /** Fire unimproving solution event at the end of the iteration. */
    protected boolean fireUnimprovingMoveMade;
    
    /** Fire improving solution event at the end of the iteration. */
    protected boolean fireImprovingMoveMade;
    
    /** Fire no change in value solution event at the end of the iteration. */
    protected boolean fireNoChangeInValueMoveMade;
    
       
    /** Choose first improving neighbor instead of best neighbor overall. */
    protected boolean chooseFirstImprovingMove = false;
    
    /** Print errors to this stream. */
    protected static java.io.PrintStream err = System.err;
    
    
/* ********  C O N S T R U C T O R S  ******** */
    
    
    /**
     * Constructs a <tt>SingleThreadedTabuSearch</tt> with no tabu objects set.
     *
     * @since 1.0
     */
    public TabuSearch()
    {
        super();
        
    }   // end constructor


	public TabuSearch(MoveManager moveManager,
            ObjectiveFunction objectiveFunction,
            LongTermMemory longTermMemory,
            boolean maximizing)
	{
		this();		

		// Set current solution to initial solution 
		// and best solution to a copy of the initial solution.
		//setCurrentSolution( initialSolution );
		//setBestSolution( (Solution) initialSolution.clone() );

		// Set tabu objects
		this.objectiveFunction = objectiveFunction;
		this.moveManager = moveManager;
		this.tabuList = tabuList;
		this.aspirationCriteria = aspirationCriteria;
		this.longTermMemory = longTermMemory;
		this.maximizing = maximizing;		

	}   // end constructor
    
    /**
     * Constructs a <tt>SingleThreadedTabuSearch</tt> with all tabu objects set.
     * The initial solution is evaluated with the objective function,
     * becomes the <tt>currentSolution</tt> 
     * and a copy becomes the <tt>bestSolution</tt>.
     *
     * @param initialSolution The initial <tt>currentSolution</tt>
     * @param moveManager The move manager
     * @param objectiveFunction The objective function
     * @param tabuList The tabu list
     * @param aspirationCriteria The aspiration criteria or <tt>null</tt> if none is to be used
     * @param maximizing Whether or not the tabu search should be maximizing the objective function
     *
     * @see Solution
     * @see ObjectiveFunction
     * @see MoveManager
     * @see TabuList
     * @see AspirationCriteria
     *
     * @since 1.0
     */
    public TabuSearch( Solution initialSolution, MoveManager moveManager,
                           ObjectiveFunction objectiveFunction, TabuList tabuList,
                           AspirationCriteria aspirationCriteria, LongTermMemory longTermMemory,
                           boolean maximizing) throws Exception
    {
        this();
        
        // Make sure initial solution is evaluated.
        double[] val = objectiveFunction.evaluate( initialSolution, null );
        initialSolution.setObjectiveValue( val );
        
        // Set current solution to initial solution 
        // and best solution to a copy of the initial solution.
        //setCurrentSolution( initialSolution );
        //setBestSolution( (Solution) initialSolution.clone() );
        this.currentSolution = initialSolution;
        this.bestSolution = (Solution)initialSolution.clone();
        
        // Set tabu objects
        this.objectiveFunction      =   objectiveFunction;
        this.moveManager            =   moveManager;
        this.tabuList               =   tabuList;
        this.aspirationCriteria     =   aspirationCriteria;
		this.longTermMemory			=	longTermMemory;
        this.maximizing             =   maximizing;
        
    }   // end constructor
    
    
    /**
     * This large method goes through one iteration. It performs these steps:
     *
     * <ul>
     *   <li>Get copies of tabu objects</li>
     *   <li>If there is no current solution, throw an exception</li>
     *   <li>If the best solution is null, set it to a copy of the current solution</li>
     *   <li>Clear event-queuing flags</li>
     *   <li>Get list of moves to try</li>
     *   <li>Find the best of those moves</li>
     *   <li>Register the best move</li>
     *   <li>Calculate new solution value using previously evaluated move's contribution</li>
     *   <li>Determine if move is unimproving</li>
     *   <li>Determine if new solution will be new best</li>
     *   <li>Operate on the current solution, making the new solution</li>
     *   <li>Set the new solution's value</li>
     *   <li>If new best, clone the solution</li>
     *   <li>Fire queued events</li>
     * </ul>
     *
     * @see #getBestMove
     * @see #isFirstBetterThanSecond
     * @see #fireQueuedEvents
     * @since 1.0
     */
    protected void performOneIteration() throws Exception, NoMovesGeneratedException, NoCurrentSolutionException
    {
        // Grab local copies of the problem.
        final   TabuList                tabuList            = getTabuList();
        final   MoveManager             moveManager         = getMoveManager();
        final   ObjectiveFunction       objectiveFunction   = getObjectiveFunction();
        final   AspirationCriteria      aspirationCriteria  = getAspirationCriteria();
        final   Solution                currentSolution     = getCurrentSolution();
                Solution                bestSolution        = getBestSolution();
        final   boolean                 chooseFirstImproving = isChooseFirstImprovingMove();
        final   boolean                 maximizing          = isMaximizing();
          
        // Check for null solutions
        if( currentSolution == null )
            throw new NoCurrentSolutionException();
        
        if( bestSolution == null )
        {   
            bestSolution = (Solution) currentSolution.clone();
            internalSetBestSolution( bestSolution );
        }   // end if: null best solution
        
        // Clear event-queuing flags.
        this.fireNewCurrentSolution      = false;
        this.fireNewBestSolution         = false;
        this.fireUnimprovingMoveMade     = false;
		this.fireImprovingMoveMade		 = false;       
        this.fireNoChangeInValueMoveMade = false;
        
        // Get list of moves to try
        final Move[] moves = moveManager.getAllMoves( currentSolution );
        if( moves == null || moves.length == 0 )
            throw new NoMovesGeneratedException();
        
        
        // Get best move. 
        // Returns an array where the first element is the best move,
        // the second element is the resulting objective function value,
        // and the third element is the tabu status.
        // This way we don't evaluate the move twice.
        final Object[] bestMoveArr = getBestMove( 
            currentSolution, moves, objectiveFunction, tabuList, 
            aspirationCriteria, maximizing, chooseFirstImproving );
        final Move bestMove = (Move)bestMoveArr[0];
        final double[] bestMoveVal = (double[])bestMoveArr[1];
        final boolean bestMoveTabu = ((Boolean)bestMoveArr[2]).booleanValue();


		
		//System.out.println(bestSolution.toString());
		//System.out.println();
		// Register the move that's about to be made with the tabu list.
        tabuList.setTabu( currentSolution, bestMove );
        
        
        // Get the old and new solution value
        final double[] oldVal = currentSolution.getObjectiveValue();
            
        
        // Determine if the new value is better or worse (equal is neither)
        if( isFirstBetterThanSecond( oldVal, bestMoveVal, maximizing ) )
            this.fireUnimprovingMoveMade = true;
        else if( isFirstBetterThanSecond( bestMoveVal, oldVal, maximizing ) )
            this.fireImprovingMoveMade = true;
        else this.fireNoChangeInValueMoveMade = true;
        
        // If the new value is improving, see if it's a new best solution
        boolean newBestSoln = false;
        if( this.fireImprovingMoveMade )
            if( isFirstBetterThanSecond( bestMoveVal, bestSolution.getObjectiveValue(), maximizing ) )
                newBestSoln = true;
        
        
        // Operate on the solution
        try{
        bestMove.operateOn( currentSolution );
        }catch(Exception e ){
            System.err.println( "Error with " + bestMove + " on " + currentSolution );
        }
        
        // Set the new solution value
        // v1.0c: Clone this array so that arrays can be reused in the objective function
        currentSolution.setObjectiveValue( (double[])bestMoveVal.clone() );

		longTermMemory.memorizeSolution(currentSolution, newBestSoln);
		
        // Update the best solution, too?
        if( newBestSoln )
        {   Solution newBest = (Solution)currentSolution.clone();
            internalSetBestSolution( newBest );
			//System.out.println("Bingo\t" + newBest.getObjectiveValue()[0]);			
        }   // end if: new best soln
					

        // Update current solution
        internalSetCurrentSolution( currentSolution );
        
        
        // Fire relevant events
        fireQueuedEvents();
        
    }   // end performOneIteration
    
         
    /**
     * Gets the best move--one that should be used for this iteration.
     * By setting <var>chooseFirstImprovingMove</var> to <tt>true</tt>
     * you tell the tabu search to return the first move it encounters
     * that is improving and non-tabu rather than search through all of
     * the moves.
     *
     * It's not static so that when the MultiThreadedTabuSearch invokes
     * the performOneIteration method the proper method is invoked.
     * Java's weird about overriding static methods...
     *
     * @since 1.0
     */
    protected  Object[] getBestMove( 
        final Solution soln,
        final Move[] moves,
        final ObjectiveFunction objectiveFunction,
        final TabuList tabuList, 
        final AspirationCriteria aspirationCriteria,
        final boolean maximizing,
        final boolean chooseFirstImprovingMove ) throws Exception
    {
        return TabuSearch.getBestMove( soln, moves, objectiveFunction,
                            tabuList, aspirationCriteria,
                            maximizing, chooseFirstImprovingMove, this );
    }   // end getBestMove
    
    
    /**
     * The static method that actually does the work. It's static so that
     * the NeighborhoodHelper in the MultiThreadedTabuSearch can
     * use the same code.
     *
     * @since 1.0
     */
    protected static Object[] getBestMove( 
        final Solution soln,
        final Move[] moves,
        final ObjectiveFunction objectiveFunction,
        final TabuList tabuList,
        final AspirationCriteria aspirationCriteria,
        final boolean maximizing,
        final boolean chooseFirstImprovingMove,
        final ITabuSearch This ) throws Exception
    {
        // Set up variables
        Move bestMove = moves[0];
        double[] bestMoveVal = {};
        boolean bestMoveTabu = false;
        
        // Set up first move
        bestMoveVal = objectiveFunction.evaluate( soln, bestMove );
        bestMoveTabu = moves.length == 0 ?
            false : // Don't bother calling the tabu list if there's only one move.
            isTabu( soln, bestMove, bestMoveVal, tabuList, aspirationCriteria, This );
        
        // If we only want to choose the first improving move,
        // we'll need to know the current solutin's value.
        // Since we will _only_ need it if we're considering
        // bailing out after the first improving move, then
        // we won't bother calling getObjectiveValue unless that is so.
        double[] currSolnVal = null;
        if( chooseFirstImprovingMove )
        {   
            currSolnVal = soln.getObjectiveValue();
            if( !bestMoveTabu && isFirstBetterThanSecond( bestMoveVal, currSolnVal, maximizing ) )
                return new Object[]{ bestMove, bestMoveVal, new Boolean( bestMoveTabu) };
        }   // end if: choose first improving
    
        // Go through each move
        final int movesLen = moves.length;
        for( int i = 1; i < movesLen; i++ )
        {            
            // Now go through the rest and see if there's a better one.
            for( i = 1; i < moves.length; i++ )
            {
                Move move = moves[i];
                
                // Since the tabu status has not yet been determined, do the
                // objective value comparisons first. Reasoning: comparing a handful
                // of doubles is likely to be faster than whatever kind of tabu
                // list the user has set up.
                double[] newObjVal = objectiveFunction.evaluate( soln, move );
                if( isFirstBetterThanSecond( newObjVal, bestMoveVal, maximizing ) )
                {   
                    // New one has a better objective value.
                    // Check the tabu status of both.
                    // Do not switch over only if the new one is tabu, but the old one isn't.
                    boolean newIsTabu = isTabu( soln, move, newObjVal, tabuList, aspirationCriteria, This );
                    
                    if( !(!bestMoveTabu && newIsTabu) )
                    {   bestMove = move;
                        bestMoveVal = newObjVal;
                        bestMoveTabu = newIsTabu;
                        
                        // If choosing first improving move, consider this one
                        if( chooseFirstImprovingMove )
                            if( !bestMoveTabu && isFirstBetterThanSecond( bestMoveVal, currSolnVal, maximizing ) )
                                return new Object[]{ bestMove, bestMoveVal, new Boolean( bestMoveTabu) };
                
                    }   // end if: switch over
                }   // end if: new one has better objective value
                else
                {   // New one does not have better objective value, but see if it
                    // has a better tabu status.
                    boolean newIsTabu;
                    if( bestMoveTabu && !isTabu( soln, move, newObjVal, tabuList, aspirationCriteria, This ) )
                    {   bestMove = move;
                        bestMoveVal = newObjVal;
                        bestMoveTabu = false;
                    }   // end if: old was tabu, new one isn't.
                }   // end else: new one does not have better objective value
            }   // end for: through remaining moves
            
        }   // end for: through each move
        
        return new Object[]{ bestMove, bestMoveVal, new Boolean( bestMoveTabu) };
    }   // end getBestMove
    
    
    
    /**
     * Determine if the move is tabu and consider whether or not it
     * satisfies the aspiration criteria.
     *
     * @since 1.0
     */
    protected static boolean isTabu( 
        final Solution soln,
        final Move move,
        final double[] val,
        final TabuList tabuList,
        final AspirationCriteria aspirationCriteria,
        final ITabuSearch This )
    {
        boolean tabu = false;
        
        // See if move is tabu
        if( tabuList.isTabu( soln, move ) )
        {   // It is tabu.
            tabu = true;
            if( aspirationCriteria != null )
            {
                // ASPIRATION CRITERIA
                // If this is better than the best, make it NOT tabu
                if( aspirationCriteria.overrideTabu( soln, move, val, This ) )
                    tabu = false;
            }   // end aspiration
        }   // end if: move was tabu
        
        return tabu;
    }   // end isTabu
    
    
    
    /**
     * Deprecated and renamed to {@link #isFirstBetterThanSecond}
     * to be named more consistently. This method still works. It simply
     * calls the newly-named version.
     *
     * @param first The first array of <tt>double</tt>s
     * @param second The second array of <tt>double</tt>s
     * @param maximizing Whether or not the tabu search should be maximizing
     * @return <tt>true</tt> if the first array of numbers is better than the second
     * @since 1.0
     * @version 1.0a
     * @deprecated
     */
    public static boolean firstIsBetterThanSecond( 
    final double[] first, final double[] second, final boolean maximizing )
    {   return isFirstBetterThanSecond( first, second, maximizing );
    }   // end firstIsBetterThanSecond
        
        

    /**
     * This single method is called many times to compare solutions.
     * Although all data is stored as doubles, they are cast to floats
     * before they are compared. This ensures that the inevitable
     * errors associated with all floating point numbers do not affect
     * the likely intent of the numbers.
     *
     * @param first The first array of <tt>double</tt>s
     * @param second The second array of <tt>double</tt>s
     * @param maximizing Whether or not the tabu search should be maximizing
     * @return <tt>true</tt> if the first array of numbers is better than the second
     * @since 1.0
     * @version 1.0a
     */
	public static boolean isFirstBetterThanSecond(
	final double[] first, final double[] second, final boolean maximizing)
	{
		int i = 0; // Put at the beginning for possible speed boost
		final int valLength = first.length;
		float first_f, second_f;

		for (i = 0; i < valLength; i++)
		{
			first_f = (float)first[i];
			second_f = (float)second[i];

			if (first_f > second_f)
				return maximizing ? true : false;

			else if (first_f < second_f)
				return maximizing ? false : true;

		}   // end for: through each value

		// If we get this far, then they're equal.
		return false;
	}   // end firstIsBetterThanSecond
    
    
    
    /**
     * Fires events that are queued for firing at the end of an iteration.
     *
     * @since 1.0
     */
    protected void fireQueuedEvents()
    {
        if( this.fireNewCurrentSolution )
        {   
            this.fireNewCurrentSolution = false;
            fireNewCurrentSolution();
        }   // end if
        
        if( this.fireNewBestSolution )
        {   
            this.fireNewBestSolution = false;
            fireNewBestSolution();
        }   // end if
        
        if( this.fireUnimprovingMoveMade )
        {   
            this.fireUnimprovingMoveMade = false;
            fireUnimprovingMoveMade();
        }   // end if: unimproving move
        else if( this.fireImprovingMoveMade )
        {   
            this.fireImprovingMoveMade = false;
            fireImprovingMoveMade();
        }   // end else if: improving move
        else
        {
            this.fireNoChangeInValueMoveMade = false;
            fireNoChangeInValueMoveMade();
        }   // end else: no change in value
        
        // If changes were made by listeners, we may need to fire the event again.
        if( this.fireNewCurrentSolution || this.fireNewBestSolution || 
            this.fireUnimprovingMoveMade || this.fireImprovingMoveMade ||
            this.fireNoChangeInValueMoveMade )
            fireQueuedEvents();
    }   // end fireQueuedEvents
    
    
    
    
    /**
     * Set the current solution and prepare to fire an event.
     *
     * @param solution The new current solution
     * @since 1.0
     */
    protected void internalSetCurrentSolution( Solution solution ) throws Exception
    {   
        this.currentSolution = solution;
        this.fireNewCurrentSolution = true;
        if( getCurrentSolution() == null )
            internalSetCurrentSolution( (Solution)solution.clone() );

		if (getCurrentSolution().getObjectiveValue() == null)
		{
			double[] val = objectiveFunction.evaluate(getCurrentSolution(), null);
			getCurrentSolution().setObjectiveValue(val);
		}
    }   // end internalSetCurrentSolution

       
    /**
     * Set the best solution and prepare to fire an event.
     *
     * @param solution The new best solution
     * @since 1.0
     */
    protected void internalSetBestSolution( Solution solution )
    {   
        this.bestSolution = solution;
        this.fireNewBestSolution = true;
    }   // end internalSetBestSolution
    
    
    /**
     * Sets the status of either solving or not solving. This does
     * not start and stop the solver--it only sets the reporting flag.
     *
     * @param solving Whether or not the tabu search should be marked as solving or not.
     * @since 1.0
     */
    protected void setSolving( boolean solving )
    {   
        this.solving = solving;
    }   // end setSolving
    
    
    /**
     * Tells the tabu search internally whether or not to keep solving
     * the next chance it gets to quit, like at the start of a new iteration.
     *
     * @param keepSolving Whether or not to keep solving
     * @since 1.0
     */
    protected void setKeepSolving( boolean keepSolving )
    {   
        this.keepSolving = keepSolving;
    }   // end setKeepSolving
    
    
    
    /**
     * Returns whether or not the tabu search should keep solving
     * the next chance it gets to quit, like at the start of a new iteration.
     *
     * @return Whether or not to keep solving
     * @since 1.0
     */
    protected boolean isKeepSolving()
    {   
        return this.keepSolving;
    }   // end isKeepSolving
    
    
    
    
    /**
     * Internally set whether or not a new current solution
     * {@link TabuSearchEvent} should be fired at the end of the iteration.
     *
     * @param b Whether or not to fire a new current solution event.
     * @since 1.0
     */
    protected void setFireNewCurrentSolution( boolean b )
    {   
        this.fireNewCurrentSolution = b;
    }   // end setFireNewCurrentSolution
    
    
    /**
     * Internally set whether or not a new best solution
     * {@link TabuSearchEvent} should be fired at the end of the iteration.
     *
     * @param b Whether or not to fire a new best solution event.
     * @since 1.0
     */
    protected void setFireNewBestSolution( boolean b )
    {   
        this.fireNewBestSolution = b;
    }   // end setFireNewBestSolution
    
    
    /**
     * Internally set whether or not an unimproving move made
     * {@link TabuSearchEvent} should be fired at the end of the iteration.
     *
     * @param b Whether or not to fire an unimproving move made event.
     * @since 1.0
     */
    protected void setFireUnimprovingMoveMade( boolean b )
    {   
        this.fireUnimprovingMoveMade = b;
    }   // end setFireUnimprovingMoveMade
    
    
    /**
     * Internally set whether or not an improving move made
     * {@link TabuSearchEvent} should be fired at the end of the iteration.
     *
     * @param b Whether or not to fire an improving move made event.
     * @since 1.0-exp7
     */
    protected void setFireImprovingMoveMade( boolean b )
    {   
        this.fireImprovingMoveMade = b;
    }   // end setFireImprovingMoveMade
    
    
    /**
     * Internally set whether or not a no change in value move made
     * {@link TabuSearchEvent} should be fired at the end of the iteration.
     *
     * @param b Whether or not to fire a no change in value move made event.
     * @since 1.0-exp7
     */
    protected void setFireNoChangeInValueMoveMade( boolean b )
    {   
        this.fireNoChangeInValueMoveMade = b;
    }   // end setFireNoChangeInValueMoveMade
    
    /**
     * Returns whether or not the tabu search plans to fire a
     * new current solution {@link TabuSearchEvent} 
     * at the end of the iteration.
     *
     * @return whether or not the tabu search plans to fire a
     *         new current solution event
     * @since 1.0
     */
    protected boolean isFireNewCurrentSolution()
    {   
        return this.fireNewCurrentSolution;
    }   // end isFireNewCurrentSolution
    
    
    /**
     * Returns whether or not the tabu search plans to fire a
     * new best solution {@link TabuSearchEvent} 
     * at the end of the iteration.
     *
     * @return whether or not the tabu search plans to fire a
     *         new best solution event
     * @since 1.0
     */
    protected boolean isFireNewBestSolution()
    {   
        return this.fireNewBestSolution;
    }   // end isFireNewBestSolution
    
    
    /**
     * Returns whether or not the tabu search plans to fire an
     * unimproving move made {@link TabuSearchEvent} 
     * at the end of the iteration.
     *
     * @return whether or not the tabu search plans to fire an
     *         unimproving move made event
     * @since 1.0
     */
    protected boolean isFireUnimprovingMoveMade()
    {   
        return this.fireUnimprovingMoveMade;
    }   // end isFireUnimprovingMoveMade
    
    /**
     * Returns whether or not the tabu search plans to fire an
     * improving move made {@link TabuSearchEvent} 
     * at the end of the iteration.
     *
     * @return whether or not the tabu search plans to fire an
     *         improving move made event
     * @since 1.0-exp7
     */
    protected boolean isFireImprovingMoveMade()
    {   
        return this.fireImprovingMoveMade;
    }   // end isFireImprovingMoveMade
    
    
    /**
     * Returns whether or not the tabu search plans to fire a
     * no change in value move made {@link TabuSearchEvent} 
     * at the end of the iteration.
     *
     * @return whether or not the tabu search plans to fire a
     *         no change in value move made event
     * @since 1.0-exp7
     */
    protected boolean isFireNoChangeInValueMoveMade()
    {   
        return this.fireNoChangeInValueMoveMade;
    }   // end isFireNoChangeInValueMoveMade
    
/* ********  T A B U S E A R C H   M E T H O D S  ******** */    
    
    /**
     * Starts the tabu search solving in the current thread, blocking
     * until the <tt>iterationsToGo</tt> property is zero.
     *
     * @since 1.0c
     */
    public void startSolving() throws Exception
    {		
        // v1.0c: Clear internal flag that might otherwise say "stop"
        setKeepSolving( true );
        
        setSolving( true ); // Set the solving flag.
        // This line was moved from below the 'if' statement
        // in v1.0-exp2 so that if an object responding
        // to the tabuSearchStarted event inquired, the
        // isSolving() method would correctly return true.
        
        // Make sure there are iterations requested.
        if( iterationsToGo > 0 )
            fireTabuSearchStarted();
        
        long startTime = System.currentTimeMillis();
        // While not canceled and iterations left to go
        while( keepSolving && ( iterationsToGo > 0 ) )
        {   
            Thread.yield();       
            synchronized( this )
            {
                iterationsToGo--;
                
                try
                {   performOneIteration();
                }   // end try
                catch( NoMovesGeneratedException e )
                {   if( err != null )
                        err.println( e );
                }   // end catch
                catch( NoCurrentSolutionException e )
                {   if( err != null )
                        err.println( e );
                }   // end catch
                incrementIterationsCompleted();
            }   // end sync: this

            if ((System.currentTimeMillis()-startTime)/1000 > timeout)
				keepSolving = false;
        }   // end while: iters left
        
        setSolving( false );
        fireTabuSearchStopped();
        
    }   // end startSolving
    
    
    /**
     * Stops the tabu search and preserves the number of
     * iterations remaining.
     *
     * @since 1.0
     */
    public synchronized void stopSolving()
    {   setKeepSolving( false );
    }   // end stopSolving
    
    
    /**
     * Returns whether or not the tabu search is currently solving.
     *
     * @since 1.0
     */
    public synchronized boolean isSolving()
    {   return solving;
    }   // end isSolving
    
    
    
    /**
     * Sets the objective function, effective at the start of the next iteration,
     * and re-evaluates the current and best solution values.
     *
     * @param function The new objective function.
     * @see ObjectiveFunction
     * @since 1.0
     */
    public synchronized void setObjectiveFunction( ObjectiveFunction function ) throws Exception
    {   this.objectiveFunction = function;
        
        if( this.currentSolution != null )
            this.currentSolution.setObjectiveValue( function.evaluate( this.currentSolution, null ) );
    
        if( this.bestSolution != null )
            this.bestSolution.setObjectiveValue( function.evaluate( this.bestSolution, null ) );
    
    }   // end setObjectiveFunction
    
    
    /**
     * Sets the move manager, effective at the start of the next iteration.
     *
     * @param moveManager The new move manager.
     * @see MoveManager
     * @since 1.0
     */
    public synchronized void setMoveManager( MoveManager moveManager )
    {   this.moveManager = moveManager;
    }   // end setMoveManager
    
    
    
    /**
     * Sets the tabu list, effective at the start of the next iteration.
     *
     * @param tabuList The new tabu list.
     * @see TabuList
     * @since 1.0
     */
    public synchronized void setTabuList( TabuList tabuList )
    {   this.tabuList = tabuList;
    }   // end setTabuList
    
    
    
    /**
     * Sets the aspiration criteria, effective at the start of the next iteration.
     * A <tt>null</tt> value means there is no aspiration criteria.
     *
     * @param aspirationCriteria The new aspiration criteria
     * @see AspirationCriteria
     * @since 1.0
     */
    public synchronized void setAspirationCriteria( AspirationCriteria aspirationCriteria )
    {   this.aspirationCriteria = aspirationCriteria;
    }   // end setAspirationCriteria
    
    
    
    /**
     * Sets the best solution, effective at the start of the next iteration,
     * and fires an event to registered {@link TabuSearchListener}s.
     *
     * @param solution The new best solution.
     * @see Solution
     * @since 1.0
     */
    public synchronized void setBestSolution( Solution solution )
    {   
        internalSetBestSolution( solution );
    }   // end setBestSolution
    
    
    
    /**
     * Sets the current solution, effective at the start of the next iteration,
     * and fires an event to registered {@link TabuSearchListener}s.
     *
     * @param solution The new current solution.
     * @see Solution
     * @since 1.0
     */
    public synchronized void setCurrentSolution( Solution solution ) throws Exception 
    {   
        internalSetCurrentSolution( solution );
    }   // end setCurrentSolution
    
    
    
    
    /**
     * Sets the number of iterations that the tabu search
     * has left to go. If the tabu search was previously idle,
     * that is <tt>iterationsToGo</tt> was less than or equal
     * to zero, the tabu search will not automatically begin again.
     * In this case the tabu search will not begin again until
     * {@link #startSolving} is called.
     *
     * @param iterations The number of iterations left for the tabu earch to execute.
     * @see #startSolving
     * @since 1.0
     */
    public synchronized void setIterationsToGo( int iterations )
    {   this.iterationsToGo = iterations;
		this.numberOfIterations = iterations;
    }   // end setIterationsToGo
    
    
    
    
    /**
     * Sets whether the tabu search should be maximizing or minimizing
     * the objective function.
     * A value of <tt>true</tt> means <em>maximize</em>.
     * A value of <tt>false</tt> means <em>minimize</em>.
     *
     * @param maximizing Whether or not the tabu search should be maximizing the objective function.
     * @since 1.0
     */
    public synchronized void setMaximizing( boolean maximizing )
    {   this.maximizing = maximizing;
    }   // end setMaximizing
    
    
    /**
     * Setting this to <tt>true</tt> will cause the search to go faster
     * by not necessarily evaluating all of the moves in a neighborhood
     * for each iteration. Instead of evaluating all of the moves and
     * selecting the best one for execution, setting this will cause
     * the tabu search engine to select the first move that it encounters
     * that causes an improvement to the current solution.
     * The default value is <tt>false</tt>.
     *
     * @param choose Whether or not the first improving move will be chosen
     * @since 1.0.1
     */
    public synchronized void setChooseFirstImprovingMove( boolean choose )
    {   this.chooseFirstImprovingMove = choose;
    }   // end setChooseFirstImprovingMove


	public void setTimeout(long seconds)
	{
		timeout = seconds;
	}
    
    /**
     * Returns the objective function being used by the tabu search.
     *
     * @return The objective function being used by the tabu search.
     * @see ObjectiveFunction
     * @since 1.0
     */
    public synchronized ObjectiveFunction getObjectiveFunction()
    {   return objectiveFunction;
    }   // end getObjectiveFunction
    
    
    
    
    
    /**
     * Returns the move manager being used by the tabu search.
     *
     * @return The move manager being used by the tabu search.
     * @see MoveManager
     * @since 1.0
     */
    public synchronized MoveManager getMoveManager()
    {   return moveManager;
    }   // end getMoveManager
    
    
    
    
    
    /**
     * Returns the tabu list being used by the tabu search.
     *
     * @return The tabu list being used by the tabu search.
     * @see TabuList
     * @since 1.0
     */
    public synchronized TabuList getTabuList()
    {   return tabuList;
    }   // end getTabuList
    
    
    
    
    /**
     * Returns the aspiration criteria.
     * A <tt>null</tt> value means there is no aspiration criteria.
     *
     * @return The aspiration criteria
     * @see AspirationCriteria
     * @since 1.0
     */
    public synchronized AspirationCriteria getAspirationCriteria()
    {   return aspirationCriteria;
    }   // end getAspirationCriteria
    
    
    
    
    /**
     * Returns the best solution found by the tabu search.
     *
     * @return The best solution found by the tabu search.
     * @see Solution
     * @since 1.0
     */
    public synchronized Solution getBestSolution()
    {   return bestSolution;
    }   // end getBestSolution
    
    
    
    
    /**
     * Returns the current solution being used by the tabu search.
     *
     * @return The current solution being used by the tabu search.
     * @see Solution
     * @since 1.0
     */
    public synchronized Solution getCurrentSolution()
    {   return currentSolution;
    }   // end getCurrentSolution
    
    
    
    
    /**
     * Returns the number of iterations left for the tabu search to execute.
     *
     * @return The number of iterations left for the tabu search to execute.
     * @since 1.0
     */
    public synchronized int getIterationsToGo()
    {   return iterationsToGo;
    }   // end getIterationsToGo
    
    
    
    
    /**
     * Returns whether or not the tabu search should be maximizing the objective function.
     *
     * @return Whether or not the tabu search should be maximizing the objective function.
     * @since 1.0
     */
    public synchronized boolean isMaximizing()
    {   return maximizing;
    }   // end isMaximizing
    
    
    /**
     * Returns whether or not the tabu search engine will choose the
     * first improving move it encounters at each iteration (<tt>true</tt>)
     * or the best move (<tt>false</tt>).
     *
     * @since 1.0.1
     */
    public synchronized boolean isChooseFirstImprovingMove()
    {   return chooseFirstImprovingMove;
    }   // end isChooseFirstImprovingMove
    
    
    
}   // end class SingleThreadedTabuSearch
