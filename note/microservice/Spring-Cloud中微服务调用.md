```flow
st=>start: Start
e=>end: End
op1=>operation: My Operation
sub1=>subroutine: MySubroutine
cond=>condition: Yes Or NO?
io=>inputoutput: catch something...
st->op1->cond
cond(yes)->io->e
cond(no)->sub1(right)->op1
```