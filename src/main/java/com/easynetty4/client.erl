%% @author a
%% @doc @todo Add description to client.


-module(client).

-export([test/2,connect/1,send/3,wait_reply/0]).
 
test(I,S) ->
	%io:format("connect succ:~p~p~n",[I,S]),
    if I =< S ->
		 timer:sleep(10),  
        spawn(fun() ->connect(I) end),
        %io:format("111connect succ:~p  ~p~n",[I,S]),
        test(I+1,S)
    end.
 
connect(Index) ->
    case gen_tcp:connect("192.168.64.173",8888,[binary,{packet,4},{reuseaddr,true}]) of
        {ok,Socket} ->
			io:format("connect succ:~p~n",[Index]),
            send(Socket, ["hello, ", integer_to_list(Index)], Index);
		
          %io:format("connect succ~n",[]);
        {error,Reason} ->
            io:format("connect failed:~p~n",[Reason])
    end.
 
send(Socket,Message, Index) ->
    %gen_tcp:send(Socket, Message),
    Reply = wait_reply(),
    io:format("Reply ~p = ~p~n",[Index,Reply]).
    %send(Socket, Message, Index).
 
wait_reply() ->
    receive
        Reply ->
            {value, Reply}
    after 10000000 ->
            timeout
    end.
