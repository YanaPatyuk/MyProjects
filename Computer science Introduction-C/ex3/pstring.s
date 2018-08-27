    # Yana Patyuk
        .data
    .section	.rodata	#read only data section
       error_msg:      .string "‫‪invalid‬‬ ‫‪input!\n‬‬"
    .text	#beginning of the code:

.global ‪pstrlen
    .type ‪pstrlen, @function
‪pstrlen:
          pushq	%rbp		# we need a save resiter to save the return address to the main function.
          movq	%rsp, %rbp	#creating the new frame pointer.
          movq     $0, %rax
          movb     (%rdi), %al        #in the first memory we have value of langth.
          movq	%rbp, %rsp	#restoring the old stack pointer.
          popq	%rbp		#restoring the save register (%rbx) value, for the caller function.          
          ret			#return to caller function (main)
.global replaceChar
    .type replaceChar, @function
    #arumnets:
    #rdi is pointer to pstring, rsi is char old rdx is new
replaceChar:
            pushq   %rbp		# we need a save resiter to save the return address to the main function.
            movq    %rsp, %rbp
            push    %rdi              #saving stirng pointer
            xorq    %rax, %rax           
            cmpb    %al, (%rdi)       #check if length is 0                 
            je .end_operation         #if length == 0 -> end.
            jmp .loop_1               #loop_1 referce to first loop in this file
            
            #do-while loop.-condintion-last byte is \0(al)
         .loop_1:
            xorq    %rcx, %rcx
            incb    %dil               #pointer++ -move pointer of string one byte
            movb    (%rdi), %cl        #copy byte to cl. cl = *pointer
            cmpb    %cl, %sil          # if cl == sil(letter to find:old char)
            je .change_char            #change the letter
            cmpb    %al, (%rdi)        #else-if *pointer ==0 (end of file)
            je .end_operation          #end.
            jmp .loop_1                #start over loop
            
            #copy new char to the string insted of old one
        .change_char:
            movb    %dl, (%rdi)        #dl is new char. *pointer = new char
            jmp .loop_1                #return to check next byte
            
            #end opertation-called when no bytes left to read
        .end_operation:
            xorq    %rax, %rax
            pop     %rax               #get pointer of the strat of pstring from stack and place in return value
            movq    %rbp, %rsp
            popq	 %rbp		 #restoring the save register (%rbx) value, for the caller function.          
            ret 
            
.global pstrijcpy
    .type pstrijcpy, @function
     #argumens:
     #rdi is pointer to p1 = first pstring, rsi is pointer to p2 = second pstring, rdx is i, rcx j
pstrijcpy:
            pushq   %rbp		# we need a save resiter to save the return address to the main function.
            movq    %rsp, %rbp
            push    %rdi              #save p1 pointer to the start of pstring in stack(we will use it in return value)
            xorq    %rax, %rax          #rax will be our counter. it counts how many steps we moved
            
            #this part check if i &j are invaild--if true-dont copy, and end.
            cmpb    $0, %cl 
            jl .invailed_ij           # j < 0
            cmpb    $0, %dl
            jl .invailed_ij           # i < 0
            cmpb    (%rdi), %cl    
            jg .invailed_ij           #p1.length < i
            cmpb    (%rdi), %dl
            jg .invailed_ij           #p1.length < j
            cmpb    (%rsi), %cl    
            jg .invailed_ij           #p2.length < i
            cmpb    (%rsi), %dl
            jg .invailed_ij           #p2.length < j
            
            #do-while loop. loop 2 regtce to secend loop in the file
        .loop_2:
            incb    %dil               #p1++-move pointer one byte
            incb    %sil               #p2++ move scond pointer one byte
            cmpb    %al, %dl           #if counter == i 
            je .startChange            #start copy
            incq    %rax               #else: counter++
            cmpb    $0, %dil           #if p1 == 0 (if we are at the end of p1)
            je .end_oper               #end the loop and return
            jmp .loop_2                #while-go back
            
        .startChange:
            movq     $0, %rbx
            movb    (%rsi), %bl        #copy first byte to rbx  bl = *p2
            movb    %bl, (%rdi)        # *p1 = *bl
            cmpb    %al, %cl           #if counter == j we copyed last byte
            je .end_oper            
            incq    %rax               #else: counter++
            incb    %dil               #p1++
            incb    %sil               #p2++
            jmp .startChange           #copy another byte
            
         #if i or j are invailrd input-bigger or smaller then strings-print massage and return
         .invailed_ij:
             movq   $0, %rdi          
             movq   $error_msg, %rdi    #put massage error in first arg of printf
             xorq   %rax, %rax
             call   printf              #print massage
             xorq   %rax, %rax          #end the operation-return
             jmp .end_oper
         #copy pointer of first pstring to rax and return. we saved pointer in the stack
        .end_oper:
            xorq    %rax, %rax
            popq    %rax                #get from stacl pointer to first pstring
            movq    %rbp, %rsp
            popq	 %rbp	            #restoring the save register (%rbx) value, for the caller function.          
            ret                         #return
            
.global swapCase
    .type swapCase, @function
swapCase:
            pushq   %rbp		   # we need a save resiter to save the return address to the main function.
            movq    %rsp, %rbp    
            xorq    %rax, %rax       
            movq    %rdi, %rax           #copy address of pstring to rax
            push    %rbx
            xorq    %rbx, %rbx
            xorq    %rcx, %rcx
            movb    (%rax), %cl          #copy length of pstring to cl
            incb    %al                  #pointer pstring++ (move one byte to start of string)
            cmp     %cl, %bl             #if length is 0 return
            je .end_fun4
        #we check in a loop the values of each letter
        #if the ascii value between 97-122 its a small letter. 65-90 big letter  
        #   ignore any other char
        .loop_3:
            cmpb    $97, (%rax)        #if *pointer >= 97 -check if its english small letter  
            jge .checkSmall
            cmpb    $65, (%rax)        #if *pointer >= 65 -check if its english big litter
            jge .checkBig
            jmp .moveToNext            #else-move to next byte and check it
         
        #this part assum that char bigger then 97 and check if smaller than 122
        .checkSmall:
            cmpb    $122, (%rax)       #if *pointer <= 122 means its small letter and we can change it
            jle .changSmallToBig
            jmp .moveToNext            #else-move to next byte and check it

            
        #this part assum that char bigger then 65 and check if smaller than 97           
        .checkBig:
            cmpb    $90 , (%rax)       #if *pointer <= 90 means its big letter and we can change it
            jle .changeBigToSmall
            jmp .moveToNext            #else-move to next byte and check it

       
                 
        .changSmallToBig:        
            subb    $32, (%rax)        #to chenge small letter to big we sub 32 from its ascii value
            jmp .moveToNext
        .changeBigToSmall:
            addb    $32, (%rax)        #to chenge big letter to small we add 32 for its ascii value
            jmp .moveToNext
                
        #move pointer to next byte
        .moveToNext:
            incb    %al                #pinter++ move pointer to string one byte to next address
            incb    %bl                #conterSteps++
            cmp     $0, (%rax)         #check if we are at the end of  string.
            je .end_fun4                   #if we are-end of operation and return
            jmp .loop_3                #else-check new char  
        .end_fun4:
            xorq    %rax, %rax
            movq    %rdi, %rax         #copy pointer to pstring addres to rax(return value)
            pop     %rbx
            movq    %rbp, %rsp
            popq	 %rbp	          #restoring the save register (%rbx) value, for the caller function.          
            ret 
