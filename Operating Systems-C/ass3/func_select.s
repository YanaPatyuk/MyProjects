    # Yana Patyuk
    .data
    .section	.rodata	#read only data section
print_len:      .string     "first pstring length: %d, second pstring length: %d\n"
print_oldNew:   .string     "old char: %c, new char: %c, first string: %s, second string: %s\n"
print_ls:       .string     "length: %d, string: %s\n"
char_f:         .string     " %c"
int_f:          .string     "%d"
wrong_input:    .string     "invalid option!\n"

    .align 8
    .text           	#beginning of the code:
.global run_func
    .type run_func, @function
run_func:
      pushq     %rbp	          # we need a save resiter to save the return address to the main function.
      movq      %rsp, %rbp	          #creating the new frame pointer.
          # Set up the jump table access
      leaq -50(%rdi),%rax       	# Compute xi = x-50
      cmpq $4, %rax                   # Compare xi:4
      ja .DefMission                  # if >, goto default-case
      jmp *.Switch_Case(,%rax,8)      # Goto jt[xi]
      

.Switch_Case:
     .quad .FirstMission              #case 50
     .quad .SecondMission             #case 51
     .quad .ThirdMission              #case 52
     .quad .ForthMission              #case 53
     .quad .DefMission                #default case-invaild input
     
      #********************************************************************************
      #***note: %rsi is pointer to first pstring & %rdx is pointer to second pstring***
      #********************************************************************************
      

      #case 50 - mission one-send each pstring to pstrlen to get length
      #note: the len is in the first byte of each pstring
.FirstMission:
        push    %rbx
        #get first length of string and put in stack
        movq    $0, %rdi          #set rdi to zero
        movq    %rsi, %rdi        #move first pstring to rdi-first parm of ‪pstrlen
        call    ‪pstrlen           #calculate len of pstring
        movq    $0,%rbx
        movb    %al, %bl          #save in stack first pstring len
        
        #get second length of string and put in stack
        movq    $0, %rdi          #set rdi to zero again
        movq    %rdx, %rdi        #move second pstring to first param od pstrlen
        call    ‪pstrlen

        
        #print the length of both strings
        #note: rax is secnd length.
        movq    $0, %rdx
        movb    %al, %dl           #move secnd length to the thirs arg of print
        movq    %rbx, %rsi         #get first string length to second param of print
        movq    $0, %rdi          
        movq    $print_len, %rdi   #passing the string the first parameter for printf.
        movq    $0,%rax
        call    printf	      #calling printf.
        movq    $0, %rax	      #change return value to zero.
        pop %rbx
        jmp .done                  #end the operation.
        
      #case 51 - mission 2
      #get 2 chars input from user and for each pstring, send.
.SecondMission:
        #get first char
        push    %r12               #save callee register
        push    %rbx               #save callee register
        push    %r8                #save callee register
        push    %rsi               #save in the stack first pstring
        push    %rdx               #save in the stack second pstring
        movq    $0, %r12
        push    %r12               #set place for input
        movq    $char_f, %rdi      #move char_f to first param scanf
        movq    %rsp, %rsi         # rsp points to start to stack-where we create space for input
                                   #move pointer to rsi for user in scanf
                                   #note:the input will saved in rsp
        movq    $0, %rax
        call    scanf              #get users char
        movq    $0, %rax       
        pop     %r12               #r12 is users input
        movq    $0, %rbx
        movb    %r12b, %bl         #store first char in rbx
        
        #get second char
        movq    $0, %r10       
        push    %r10               #allocate memory for users 
        movq    $0, %rdi
        movq    $char_f, %rdi      #move char_f to first param scanf
        movq    %rsp, %rsi         # move mem to second param
        movq    $0, %rax
        call    scanf
        movq    $0, %rax
        movq    $0, %r10
        pop     %r10
        movb    %r10b, %al         #store secend char in rax
        
        #send each pstring to change its chars by "replaceChar"
        movq    $0, %rsi
        movb    %bl, %sil          #set src char to secnd param of replaceChar
        movq    $0, %rdx
        movb    %al, %dl           #set dst char to third param of replaceChar   
        movq    $0, %rax
        pop     %rdi               #get from stack the sendd pstring and place in first param
        call    replaceChar
        pop     %rdi               #get first pstring from stack
        push    %rax               #push second pstring back to stack after being operated.
                                   #note: if we did it backword-we loose first string in stack
        movq    $0, %rax
        call    replaceChar
        movq    %rax, %rcx         #place the first pstring in the thirs param of print
        
        #print the returned values
        pop     %r8                #get from the stack second pstring and put ins titth param
        incb    %cl                #move pointer of pstring one byte to print only string without length
        incb    %r8b               #move pointer of pstring one byte to print only string without length
        movq    $print_oldNew, %rdi#move format of print 
        movq    $0, %rax
        call    printf
        movq    $0, %r8
        pop     %r8               #get from stack callee register
        pop     %rbx              #get from stack callee register
        pop     %r12              #get from stack callee register
        jmp .done                 #end of operation.
        
      #case 52
      #get two numbers fron user, copy  and print.
.ThirdMission:
        push    %r12             #save callee register
        push    %rbx             #save callee register
        push    %rsi             #save pointer of first pstring in the stack
        push    %rdx             #save pointer of secnd pstring in the stack
        #get first number i
        movq    $0, %r12       
        push    %r12             #push register to stack for allocate memory
        movq    $int_f, %rdi     #move int_f to first param scanf
        movq    %rsp, %rsi       #move rsp to second param. rsp pointer to input of user.
        movq    $0, %rax
        call    scanf
        movq    $0, %rax
        movq    $0, %r12
        pop     %r12             #first pointer in stack is users number
        movq    $0, %rbx       
        movb    %r12b, %bl       #store firat number i in rbx 
        
        #get second number j
        movq    $0, %r10
        push    %r10            #allocate memory fot input in stack. 
        movq    $0, %rdi
        movq    $int_f, %rdi    #move int f to first param scanf-"%d"
        movq    %rsp, %rsi      # move rsp to second param of scanf. rsp pointer to top of stack
        movq    $0, %rax
        call    scanf
        movq    $0, %rax
        movq    $0, %r10
        pop     %r10            #the top of stack points on users input
        movb    %r10b, %al      #save users input i in rax
        
        #call pstrijcpy. send all needed paramters for pstrijcpy
        pop     %rsi            #get secnd pstrint and save it as secnd param for pstrijcpy
        pop     %rdi            #get first pstring and save it as first param for pstrijcpy
        movq    $0, %r12
        movq    %rsi, %r12        #get a copy of secnd param pointer for print it and the end
        movq    $0, %rcx
        movq    $0, %rdx
        movq    %rax, %rcx         #move j to rcx
        movq    %rbx, %rdx         #move i to rdx 
        call    pstrijcpy

        #print the first pstring
        movq    $0, %rsi
        movb    (%rax), %sil       #rax points to pstring. first byte is length. copy to sil
        incb    %al                #move pointer one byte. now rax points on pstring's string
        movq    $0, %rdx
        movq    %rax, %rdx         #move pointer of string to rdx-as thirs param for print
        movq    $print_ls, %rdi    #move foramt of printing
        movq    $0, %rax
        call    printf             #print
        movq    $0, %rax
        
        
        #print the secnd pstring
        movq    $0, %rsi
        movb    (%r12), %sil       #r12 points to pstring. first byte is length. copy to sil
        incb    %r12b              #move pointer one byte. now rax points on pstring's string
        movq    $0, %rdx
        movq    %r12, %rdx         #move pointer of string to rdx-as thirs param for print
        movq    $print_ls, %rdi    #move foramt of printing
        movq    $0, %rax
        call    printf             #print
        pop     %rbx               #get from stack callee register
        pop     %r12               #get from stack callee register
        jmp .done                  #end of operation.
        
      #case 53
.ForthMission:

        pushq   %rdx               #save secnd string in stack
        movq    $0, %rdi
        movq    %rsi, %rdi         #move first string to rdi-first param of swapCase
        call    swapCase           
        movq    $0, %rsi
        movb    (%rax), %sil       #get first string length
        incb    %al                #move pointer to next byte-place string strats
        movq    $0, %rdx
        movq    %rax, %rdx         #move the pointer of pstring's string, to third arg of print
        movq    $0, %rdi
        movq    $print_ls, %rdi    #move format of print
        movq    $0, %rax
        call    printf
        movq    $0, %rax
        
        xorq    %rdi, %rdi
        popq    %rdi               #get secnd pstring fron stack and place in rdi  as first arg of swap
        call    swapCase   
        movq    $0, %rsi
        movb    (%rax), %sil       #get secnd's pstring length
        incb    %al                #move pointer to next byte-place string strats
        movq    $0, %rdx
        movq    %rax, %rdx         #move the pointer of pstring's string, to third arg of print
        movq    $print_ls, %rdi    #move format of print
        movq    $0, %rax
        call    printf
        movq    $0, %rax
        jmp .done                 #end of operation
        
      #case def: is invaild input
.DefMission:
        movq	$wrong_input,%rdi	#passing the error massage to the first parameter for printf.
        movq	$0,%rax
        call	printf		#calling printf.
        movq	$0, %rax	         #return value is zero.
        jmp .done
        
      # done-ends the procces and return to main
.done:
         movq	$0, %rax	          #return value is zero.
         movq	%rbp, %rsp	#restoring the old stack pointer.
         popq	%rbp		#restoring the save register (%rbx) value, for the caller function.          
         ret			#return to caller function (main)
