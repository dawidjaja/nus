# arrayCount.asm
  .data 
arrayA: .word 8, 8, 8, 8, 8, 8, 8, 8   # arrayA has 5 values
count:  .word 0             # dummy value

  .text
main:
    # code to setup the variable mappings
    add $zero, $zero, $zero  #dummy instructions, can be removed
    add $zero, $zero, $zero  #dummy instructions, can be removed
    add $zero, $zero, $zero  #dummy instructions, can be removed
    la $t0, arrayA
    la $t1, count
    lw $t5, 0($t1) #value of count

    # code for reading in the user value X
    li $v0, 5
    syscall
    add $t0, $zero, $v0 #x stored in t0

    # code for counting multiples of X in arrayA
    addi $t1, $zero, 0
    addi $t2, $zero, 32 #endpoint
    la $t3, arrayA
    addi $t8, $t0, -1 #mask

loop:
    bge $t1, $t2, end
    add $t6, $t1, $t3
    lw $t7, 0($t6)

    and $t4, $t7, $t8
    bne $t4, $zero, skip
    addi $t5, $t5, 1
skip:
    addi $t1, $t1, 4
    j loop
end: 

    # code for printing result
    li   $v0, 1    # system call code for print_int
    add $a0, $zero, $t5
    syscall        # print the integer

    # code for terminating program
    li  $v0, 10
    syscall
