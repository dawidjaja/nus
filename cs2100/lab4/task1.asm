# messages.asm
  .data 
str: .asciiz "the answer = "
  .text

main: 
    li   $v0, 5
    syscall
    add  $t0, $zero, $v0
    
    li   $v0, 4    # system call code for print_string
    la   $a0, str  # address of string to print
    syscall        # print the string

    li   $v0, 1    # system call code for print_int
    add $a0, $zero, $t0
    syscall        # print the integer
    
    li   $v0, 10   # system call code for exit
    syscall        # terminate program
