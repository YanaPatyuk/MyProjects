    # Yana Patyuk
     .data
    .section	.rodata	#read only data section
int_f:          .string     "%d"
string_f:       .string     "%s"


    .text    # start code here
    .type run_func, @function
.global main
main:
    movq %rsp, %rbp #for correct debugging
    pushq	  %rbp            #we need a save resiter to save the return address to the main function.
    pushq   %rbx            #save callee register
    pushq   %r12            #save callee register
    movq    %rsp, %rbp      #creating the new frame pointer.
   #empty registar 0x0
    xorq    %rax, %rax
    xorq    %r12, %r12
    xorq    %rbx, %rbx
    
    #get first string length
    subb    $4, %spl       #allocate 4 byte(int size) in stack for users input
    xorq    %rdi, %rdi
    movq    $int_f, %rdi   #move int_f to first &arg of scanf
    movq    %rsp, %rsi     # rsp points to the memory we allocated-move pointer for input to rsi.
    xorq    %rax, %rax
    call    scanf
    xorq    %rax, %rax
    movb    (%rsp), %al    #move the value of number to rax register.
    addb    $4 ,%spl       #dis-allocate memory on stack
    
    #get string
    movq    %rax, %rbx     #rsave the length in rbp register. we change rax's value.
    incb    %al            #add 1 for the length to place \0
    subq    %rax, %rsp     #allocate memory in stack by the given length of string.
    xorq    %rdi, %rdi
    movq    $string_f, %rdi#move char_f to first param scanf
    movq    %rsp, %rsi     #rsp points to the memory we allocated-move pointer for input to rsi.
    xorq    %rax, %rax
    call    scanf
    xorq    %rax, %rax     #note:at this point rsp points to the strat of the string. we need to add length.
    subb    $1, %spl       #allocate memory for length in stack.spl+1 will be the start of string now
    movb    %bl, (%rsp)    #move the length to rsp to the start.
    movq    $0, %rbx
    movq    %rsp, %rbx     #save the start of first  pstring in rbx(caller)
    
       #get secnd string length
    subb    $4, %spl       #allocate 4 byte(int size) in stack for users input
    xorq    %rdi, %rdi
    movq    $int_f, %rdi   #move int_f to first &arg of scanf
    movq    %rsp, %rsi     #rsp points to the memory we allocated-move pointer for input to rsi.
    xorq    %rax, %rax
    call    scanf
    xorq    %rax, %rax
    movb    (%rsp), %al    #move the value of number to rax register.
    addb    $4 ,%spl       #dis-allocate memory on stack
    
    #get string
    movq    %rax, %r12     #rsave the length in rbp register. we change rax's value.
    incb    %al            #add 1 for the length to place \0
    subq    %rax, %rsp     #allocate memory in stack by the given length of string.
    xorq    %rdi, %rdi
    movq    $string_f, %rdi#move char_f to first param scanf
    movq    %rsp, %rsi     #rsp points to the memory we allocated-move pointer for input to rsi.
    xorq    %rax, %rax
    call    scanf
    xorq    %rax, %rax      #note:at this point rsp points to the strat of the string. we need to add length.
    subb    $1, %spl        #allocate memory for length in stack.spl+1 will be the start of string now
    movb    %r12b, (%rsp)   #move the length to rsp to the start.
    movq    $0, %r12
    movq    %rsp, %r12     #save the start of first  pstring in rbx(caller)
    
    
    #get number of operation
    subb    $4, %spl       #allocate 4 byte(int size) in stack for users input
    xorq    %rdi, %rdi
    movq    $int_f, %rdi   #move int_f to first &arg of scanf
    movq    %rsp, %rsi     # rsp points to the memory we allocated-move pointer for input to rsi.
    xorq    %rax, %rax
    call    scanf
    xorq    %rax, %rax
    movb    (%rsp), %al    #move the value of number to rax register.
    addb    $4 ,%spl       #dis-allocate memory on stack
    
    #place argumnets and call run func
    xorq    %rdi, %rdi
    movb    %al, %dil       #move users operation choice to forst param of run func
    movq    %r12, %rdx      #move secnd pstring to third param
    movq    %rbx, %rsi      #move first pstring to secnd param
    call    run_func
    
    xorq    %rax, %rax
    movq    %rbp, %rsp       #place rso back to place it was in the start      
    popq    %r12             #get callee register
    popq    %rbx             #get callee register
    popq	   %rbp		#restoring the save register (%rbx) value, for the caller function.         
    ret
    