import { TagService } from "@/api/tag.service.ts";
import { useParams } from "react-router-dom";
import { MouseEventHandler, useState } from "react";
import { Tag, TagCreateData, TagCreateSchema } from "@/types/Tag.ts";
import { Badge } from "@/components/ui/badge.tsx";
import { Button } from "@/components/ui/button.tsx";
import { Input } from "@/components/ui/input.tsx";
import { SubmitHandler, useForm } from "react-hook-form";
import { Label } from "@/components/ui/label.tsx";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogTrigger } from "@/components/ui/dialog.tsx";
import GenericForm, { SubmitFn } from "@/components/GenericForm.tsx";
import { keepPreviousData, UseQueryResult } from "@tanstack/react-query";

const AdminTagListing = () => {
  const [dialogOpen, setDialogOpen] = useState(false);
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const tagsQuery = TagService.useGetAll(
    {
      courseYearId,
    },
    {
      placeholderData: keepPreviousData,
    },
  ) as UseQueryResult<Tag[]>;
  const { mutate: createTag } = TagService.useCreate();
  const handleCreate: SubmitFn<TagCreateData> = (data) =>
    new Promise((resolve) =>
      createTag(data, {
        onSuccess: () => resolve(undefined),
        onError: (error) => resolve({ type: error.getStatus(), message: error.getErrorMessage() }),
      }),
    );

  return (
    <div className="max-w-[50%]">
      <div className="flex justify-between mb-4">
        <h4>Tags</h4>
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
          <DialogTrigger asChild>
            <Button className="h-auto">Create Tag</Button>
          </DialogTrigger>
          <DialogContent>
            <GenericForm
              schema={TagCreateSchema}
              defaultValues={{
                courseYearId: parseInt(courseYearId!),
                name: "",
              }}
              onSubmit={handleCreate}
              title={"Create tag"}
              closeDialog={() => setDialogOpen(false)}
            />
          </DialogContent>
        </Dialog>
      </div>
      <div className="space-y-1">
        {tagsQuery.data?.map((tag) => <TagItem key={tag.id} tag={tag} />)}
      </div>
    </div>
  );
};

interface TagItemProps {
  tag: Tag;
}

interface TagItemForm {
  name: string;
}

const TagItem = ({ tag }: TagItemProps) => {
  const { mutate: deleteTag } = TagService.useDeleteById();
  const { mutate: updateTag } = TagService.useUpdateById();
  const form = useForm<TagItemForm>({
    defaultValues: {
      name: tag.name,
    },
  });
  const [isEditing, setIsEditing] = useState(false);
  const enableEditing = () => {
    setIsEditing(true);
    setTimeout(() => form.setFocus("name"), 0);
  };

  const cancelEditing: MouseEventHandler = (e) => {
    e.preventDefault();
    setIsEditing(false);
    form.reset();
  };
  const handleDeleteClick = () => {
    deleteTag(
      { id: tag.id },
      {
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );
  };

  const onSubmit: SubmitHandler<TagItemForm> = (data) => {
    if (data.name === tag.name) {
      setIsEditing(false);
      return;
    }
    updateTag(
      {
        id: tag.id,
        ...data,
      },
      {
        onSuccess: () => {
          setIsEditing(false);
          form.reset({ name: data.name });
        },
        onError: (error) => toast.error(error.getErrorMessage()),
      },
    );
  };

  return (
    <div className="flex flex-col py-2 px-4 border rounded-md gap-2">
      <div className="flex justify-between items-center">
        <Badge variant="outline">{tag.name}</Badge>
        <div className="flex items-center gap-2">
          {!isEditing && (
            <Button variant="outline" className="h-auto py-1 px-2" onClick={enableEditing}>
              Edit
            </Button>
          )}
          <Button variant="destructive" className="h-auto py-1 px-2" onClick={handleDeleteClick}>
            Delete
          </Button>
        </div>
      </div>
      {isEditing && (
        <form onSubmit={form.handleSubmit(onSubmit)} className="flex justify-between items-end">
          <div>
            <Label htmlFor={`name-${tag.id}`}>Name</Label>
            <Input
              id={`name-${tag.id}`}
              type="text"
              className="py-1 max-w-[200px] text-sm h-auto mt-1"
              {...form.register("name")}
            />
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              className="h-auto py-1.5"
              onClick={cancelEditing}
              type="reset"
            >
              Cancel
            </Button>
            <Button className="h-auto py-1.5" type="submit">
              Save
            </Button>
          </div>
        </form>
      )}
    </div>
  );
};

export default AdminTagListing;
