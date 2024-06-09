import { SubmitHandler, useForm } from "react-hook-form";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../ui/form";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { LoaderCircle } from "lucide-react";
import { VALID_FILE_TYPES } from "@/types/Common.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { MaterialCreateData, MaterialCreateSchema } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";
import { useParams } from "react-router-dom";
import { TagService } from "@/api/tag.service.ts";
import { MultiSelect } from "@/components/ui/multi-select.tsx";
import { useMemo } from "react";
import { Combobox } from "@/components/ui/combobox.tsx";
import { MaterialGroupService } from "@/api/material-group.service.ts";

interface MaterialCreateFormProps {
  materialGroupId?: number;
  closeDialog: () => void;
}

const MaterialCreateForm = ({ materialGroupId, closeDialog }: MaterialCreateFormProps) => {
  const { courseYearId } = useParams<{ courseYearId: string }>();
  const tagsQuery = TagService.useGetAll({
    courseYearId,
  });

  const groupsQuery = MaterialGroupService.useGetAll({
    courseYearId,
  });

  const groupsOptions = useMemo(() => {
    return (
      groupsQuery.data?.content.map((group) => ({
        label: group.name,
        value: group.id.toString(),
      })) ?? []
    );
  }, [groupsQuery.data]);

  const tagOptions = useMemo(() => {
    return (
      tagsQuery.data?.map((tag) => ({
        label: tag.name,
        value: tag.id.toString(),
      })) ?? []
    );
  }, [tagsQuery.data]);

  const { mutate: createMaterial } = MaterialService.useCreate();
  const form = useForm<MaterialCreateData>({
    defaultValues: {
      materialGroupId: materialGroupId ?? undefined,
      name: "",
      description: "",
      tagIds: [],
    },
    resolver: zodResolver(MaterialCreateSchema),
    mode: "onTouched",
  });

  const onSubmit: SubmitHandler<MaterialCreateData> = (data) =>
    new Promise<void>((resolve) => {
      createMaterial(data, {
        onSuccess: () => {
          closeDialog();
          resolve();
        },
        onError: (error) => {
          form.setError("root.serverError", {
            type: error.getStatusCode().toString(),
            message: error.getErrorMessage(),
          });
          resolve();
        },
      });
    });
  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="flex flex-col gap-2"
        autoComplete="off"
      >
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name:</FormLabel>
              <FormControl>
                <Input {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="file"
          render={({ field: { value, onChange, ...fieldProps } }) => (
            <FormItem>
              <FormLabel>File:</FormLabel>
              <FormControl>
                <Input
                  {...fieldProps}
                  accept={`${VALID_FILE_TYPES.join(",")},.md`}
                  placeholder="Choose file"
                  type="file"
                  onChange={(event) => onChange(event.target.files)}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="materialGroupId"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Group</FormLabel>
              <FormControl>
                <Combobox
                  options={groupsOptions}
                  onValueChange={field.onChange}
                  value={field.value?.toString()}
                  placeholder="Select group..."
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="tagIds"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Tags</FormLabel>
              <FormControl>
                <MultiSelect
                  options={tagOptions}
                  onValueChange={field.onChange}
                  placeholder="Select tags"
                  defaultValue={field.value!.map((tag) => tag.toString())}
                />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Description:</FormLabel>
              <FormControl>
                <Input {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        {form.formState.errors.root?.serverError.message && (
          <p className="text-destructive text-sm col-span-2">
            {form.formState.errors.root?.serverError.message}
          </p>
        )}
        <div className="flex flex-row-reverse col-span-2">
          <Button
            type="submit"
            disabled={form.formState.isSubmitting || !form.formState.isDirty}
            className="w-[5rem]"
          >
            {form.formState.isSubmitting ? <LoaderCircle className="animate-spin" /> : "Submit"}
          </Button>
        </div>
      </form>
    </Form>
  );
};

export default MaterialCreateForm;
