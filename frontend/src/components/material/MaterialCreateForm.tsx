import { SubmitHandler, useForm } from "react-hook-form";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "../ui/form";
import { Input } from "../ui/input";
import { Button } from "../ui/button";
import { LoaderCircle } from "lucide-react";
import { VALID_FILE_TYPES } from "@/types/Common.ts";
import { zodResolver } from "@hookform/resolvers/zod";
import { MaterialCreateData, MaterialCreateSchema } from "@/types/Material.ts";
import { MaterialService } from "@/api/material.service.ts";

interface MaterialCreateFormProps {
	materialGroupId: number;
}

const MaterialCreateForm = ({ materialGroupId }: MaterialCreateFormProps) => {
	const { mutate: createMaterial } = MaterialService.useCreate();
	const form = useForm<MaterialCreateData>({
		defaultValues: {
			materialGroupId,
			name: "",
			description: "",
		},
		resolver: zodResolver(MaterialCreateSchema),
		mode: "onTouched",
	});

	const onSubmit: SubmitHandler<MaterialCreateData> = (data) =>
		new Promise<void>((resolve) =>
			createMaterial(data, {
				onSuccess: () => {
					form.reset();
					resolve();
				},
				onError: (error) => {
					form.setError("root.serverError", {
						type: error.getStatusCode().toString(),
						message: error.getErrorMessage(),
					});
					resolve();
				},
			}),
		);
	return (
		<Form {...form}>
			<h3 className="font-semibold">Create Material</h3>
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
