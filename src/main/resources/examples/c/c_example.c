int main()
{
    int n = 0;
    if( a < 20 ) {
          printf("a is less than 20\n" );
       }

    switch(expression) {

       case constant-expression  :
          statement(s);
          break; /* optional */
       default : /* Optional */
       statement(s);
    }

    while(condition) {
       statement(s);
    }

    for ( init; condition; increment ) {
       statement(s);
    }

    /* Create window with name "Setup" and top-left corner at (0,0) */

    set_up_dialog("Setup", 0, 0);

    /* Display the window and read the results */

    read_dialog_window();

    /* Print out the new values */

    printf("n = %d, x = %f\n", n, x);
}

statement(int s)
{
    printf("s");
}
}
}
