import { useParams } from "react-router-dom";
import { MaterialService } from "@/api/material.service.ts";
import MaterialItem from "@/components/material/MaterialItem.tsx";
import { Skeleton } from "@/components/ui/skeleton.tsx";
import { CommentService } from "@/api/comment.service.ts";
import { CommentEditSchema, CommentSort, MaterialComment } from "@/types/MaterialComment.ts";
import { useMemo, useState } from "react";
import { Separator } from "@/components/ui/separator.tsx";
import { Check, Pencil, Trash } from "lucide-react";
import { Button } from "@/components/ui/button.tsx";
import { Textarea } from "@/components/ui/textarea.tsx";
import { Label } from "@/components/ui/label.tsx";
import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input.tsx";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import { keepPreviousData } from "@tanstack/react-query";

const MaterialPage = () => {
  const [sortValue, setSortValue] = useState(CommentSort[0].value);
  const { materialId } = useParams<{ materialId: string }>();
  const materialQuery = MaterialService.useGetById(materialId ? parseInt(materialId) : undefined);
  const commentQuery = CommentService.useGetAll(
    {
      materialId: materialId ? parseInt(materialId) : undefined,
      sortField: sortValue.split("-")[0],
      sortDirection: sortValue.split("-")[1],
    },
    {
      placeholderData: keepPreviousData,
    },
  );
  return (
    <div className="container min-w-[325px] ">
      {materialQuery.data ? (
        <MaterialItem material={materialQuery.data} showCommentsLink={false} />
      ) : (
        <Skeleton className="h-20 p-4" />
      )}
      <div className="mt-4 space-y-4 md:max-w-[70%]">
        <CommentInput materialId={parseInt(materialId!, 10)} />
        <div className="flex justify-between flex-col md:flex-row">
          <h4 className="text-lg font-bold">Comments ({commentQuery.data?.totalElements})</h4>
          <Select onValueChange={setSortValue} defaultValue={sortValue}>
            <SelectTrigger className="max-w-[200px] overflow-hidden whitespace-nowrap">
              <div>
                <span>Sort by: </span>
                <SelectValue placeholder="" className="overflow-hidden" />
              </div>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {CommentSort.map((sort) => (
                  <SelectItem key={sort.value} value={sort.value}>
                    {sort.label}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          {commentQuery.data?.content.map((comment) => (
            <CommentItem key={comment.id} comment={comment} />
          ))}
        </div>
      </div>
    </div>
  );
};

const CommentInput = ({ materialId }: { materialId: number }) => {
  const { mutate: createComment } = CommentService.useCreate();
  const form = useForm<CommentItemForm>({
    mode: "onTouched",
    resolver: zodResolver(CommentEditSchema),
  });

  const submitHandler: SubmitHandler<CommentItemForm> = (data) => {
    createComment(
      { text: data.text, materialId },
      {
        onSuccess: () => {
          form.reset({ text: "" });
          setIsFocused(false);
        },
      },
    );
  };

  const handleCancel = () => {
    setIsFocused(false);
    form.reset({ text: "" });
  };

  const [isFocused, setIsFocused] = useState(false);

  const onFocusIn = () => {
    setIsFocused(true);
    setTimeout(() => form.setFocus("text"));
  };

  return (
    <form
      onSubmit={form.handleSubmit(submitHandler)}
      className="flex flex-col space-x-2 border
      p-1 rounded-lg ring-offset-background has-[:focus-visible]:ring-2 has-[:focus-visible]:ring-ring has-[:focus-visible]:ring-offset-2"
    >
      {!isFocused ? (
        <Input placeholder="Add a comment" onFocus={onFocusIn} className="border-none" />
      ) : (
        <>
          <Textarea
            placeholder="Add a comment"
            id="message"
            {...form.register("text")}
            className="border-none  focus-visible:ring-0 focus-visible:ring-offset-0"
          />
          <div className="self-end flex gap-4">
            <Button type="reset" className="self-end mt-2" variant="outline" onClick={handleCancel}>
              Cancel
            </Button>
            <Button type="submit" className="self-end mt-2">
              Comment
            </Button>
          </div>
        </>
      )}
    </form>
  );
};

interface CommentItemProps {
  comment: MaterialComment;
}

interface CommentItemForm {
  text: string;
}

const CommentItem = ({ comment }: CommentItemProps) => {
  const { mutate: updateComment } = CommentService.useUpdateById();
  const form = useForm<CommentItemForm>({
    defaultValues: {
      text: comment.text,
    },
    mode: "onTouched",
    resolver: zodResolver(CommentEditSchema),
  });

  const submitHandler: SubmitHandler<CommentItemForm> = (data) => {
    if (data.text === comment.text) {
      setIsEditing(false);
      return;
    }
    updateComment(
      { id: comment.id, text: data.text },
      {
        onSuccess: () => {
          form.reset({ text: data.text });
          setIsEditing(false);
        },
      },
    );
  };

  const localTime = useMemo(
    () => new Date(comment.createdAt).toLocaleString(),
    [comment.createdAt],
  );
  const [isEditing, setIsEditing] = useState(false);
  return (
    <form
      onSubmit={form.handleSubmit(submitHandler)}
      className="flex flex-col space-x-2 p-4 border rounded-sm"
    >
      <div className="flex justify-between flex-col md:flex-row">
        <label className="flex items-center h-6">
          <span className="md:text-lg font-bold">{comment.username} </span>
          <Separator className="mx-2" orientation="vertical" />
          <span className="text-xs text-muted-foreground">{localTime}</span>
        </label>
        {comment.currentUser && (
          <div className="flex gap-2">
            {isEditing ? (
              <Button variant="ghost" className="p-1 h-auto" type="submit">
                <Check className="w-5 h-5" />
              </Button>
            ) : (
              <Button
                variant="ghost"
                className="p-1 h-auto"
                type="button"
                onClick={(e) => {
                  e.preventDefault();
                  setIsEditing(true);
                }}
              >
                <Pencil className="w-4 h-4" />
              </Button>
            )}
            <Button variant="ghost" className="p-1 h-auto" type="button">
              <Trash className="w-4 h-4 text-destructive" />
            </Button>
          </div>
        )}
      </div>
      {isEditing ? (
        <div className="grid w-full gap-1.5 mt-2">
          <Label htmlFor="message" className="text-muted-foreground">
            Your comment:
          </Label>
          <Textarea placeholder="Type your message here." id="message" {...form.register("text")} />
        </div>
      ) : (
        <>
          <p>{comment.text}</p>
          {comment.edited && (
            <span className="text-muted-foreground text-xs mt-1 italic">(Edited)</span>
          )}
        </>
      )}
    </form>
  );
};

export default MaterialPage;
