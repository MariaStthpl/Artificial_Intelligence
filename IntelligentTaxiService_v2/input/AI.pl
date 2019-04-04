taxiIsAvailable(Tid1) :- taxi(_,_,Tid,A,_,_,_,_,_), A = 'yes', atom_number(Tid,Tid1).
taxiCapacity(Tid,Persons):- taxi(_,_,Tid,_,C,_,_,_,_), split_string(C,"-","",[Low,Max|_]), atom_number(Persons,P), atom_number(Low,L), atom_number(Max,M),P>=L, P=<M.
taxiLanguage(Tid,Language):-taxi(_,_,Tid,_,_,L,_,_,_), L=Language.
taxiRating(Tid,R):-taxi(_,_,Tid,_,_,_,R,_,_).
taxiLongDistance(Tid,LD):-taxi(_,_,Tid,_,_,_,_,LD,_).
taxiType(Tid,T):-taxi(_,_,Tid,_,_,_,_,_,T).
taxiIsSuitable(Tid) :- client(_,_,_,_,_,Persons,Language,_), taxiIsAvailable(Tid), taxiCapacity(Tid,Persons), taxiLanguage(Tid,Language).




all_street_nodes(L2,[H|L],LID):- len([H|L],N), number_of_nodes(LID,N), is_node(H,LID), is_not_in_list(H,L2),myappend(L2,H,L3), all_street_nodes_next(L3,L,LID),!.
all_street_nodes_next(_,[],_).
all_street_nodes_next(L2,[H|L],LID):- is_node(H,LID), is_not_in_list(H,L2),myappend(L2,H,L3), all_street_nodes_next(L3,L,LID).


is_node(H,LID):-nodes(_,_,LID,H,_).


is_not_in_list(_,[]).
is_not_in_list(H,[H2|L]):-H\=H2, is_not_in_list(H,L).

len([],0).
len([_|L],N):-len(L,N2), X is N2+1, N=X.

myappend([],H,[H]).
myappend([X|L2],H,[X|L3]):-myappend(L2,H,L3).

number_of_nodes(LID,N):-aggregate_all(count,is_node(_,LID),N).


nodes2(X,Y,LID,NID,N):-nodes(X1,Y1,LID,NID1,N), atom_number(X1,X), atom_number(Y1,Y), atom_number(NID1,NID).
client2(X,Y,Xd,Yd,_,_,_,_) :- client(X1,Y1,Xd1,Yd1,_,_,_,_),  atom_number(X1,X), atom_number(Y1,Y), atom_number(Xd1, Xd), atom_number(Yd1,Yd).
taxi2(X,Y,_,_,_,_,_,_,_):-taxi(X1,Y1,_,_,_,_,_,_,_), atom_number(X1,X), atom_number(Y1,Y).
 
epomeno(X,Y):- is_node(X,LID), all_street_nodes([],L,LID), is_node(Y,LID), einai_sthn_seira(X,Y,L),
(lines(LID,_,_,'yes',_,_,_,_,_,_,_,_,_,_,_,_,_,_);lines(LID,_,_,'no',_,_,_,_,_,_,_,_,_,_,_,_,_,_)).

prohgoumeno(X,Y):- is_node(X,LID),all_street_nodes([],L,LID),  is_node(Y,LID), einai_sthn_seira(Y,X,L),
(lines(LID,_,_,'-1',_,_,_,_,_,_,_,_,_,_,_,_,_,_);lines(LID,_,_,'no',_,_,_,_,_,_,_,_,_,_,_,_,_,_)).

einai_sthn_seira(X,Y,[X1,Y2|L]):-X1=X, Y2=Y,!;einai_sthn_seira(X,Y,[Y2|L]).

geitones(X,Y,Z,LID):-(epomeno(X,Y),is_node(Y,LID);Y='0'),(prohgoumeno(X,Z),is_node(Z,LID);Z='0'),Y\=Z,!.

all_geitones(X,Y,Z):- is_node(X,LID), geitones(X,Y1,Z1,LID) , atom_number(Y1,Y), atom_number(Z1,Z).




epomeno2(X,Y):- is_node(X,LID), all_street_nodes([],L,LID), is_node(Y,LID), einai_sthn_seira(X,Y,L).
prohgoumeno2(X,Y):- is_node(X,LID),all_street_nodes([],L,LID),  is_node(Y,LID), einai_sthn_seira(Y,X,L).
vres_kontinotero_dromo(ID1,NID1):- atom_number(ID1,ID1), atom_number(NID,NID1), is_node(ID,LID), lines(LID,_,_,X,_,_,_,_,_,_,_,_,_,_,_,_,_,_), (X='yes';X='no',X='-1'), NID=ID,!;
epomeno2(ID,E), vres_kontinotero_dromo(E,NID),!;prohgoumeno2(ID,P), vres_kontinotero_dromo(P,NID),!.

parametroi_A_star(ID,T,F):- is_node(ID,LID), lines(LID,_,_,_,F2,_,T2,_,_,_,_,_,_,_,_,_,_,_), (F2='yes',F='-1';F2\='yes',F='1'),(T2='',T='30';T2\='',T=T2).


distance(X,Y,NX,NY,D):- D is sqrt((X-NX)*(X-NX)+(Y-NY)*(Y-NY)).



checkan(X):- atom_number('1',X).
checkss(H):-atomic_list_concat([He|_],'-','1-4'), atom_number(He,H).






